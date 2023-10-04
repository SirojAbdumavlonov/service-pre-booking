package com.example.preordering.service;

import com.example.preordering.constants.CompanyFunctionality;
import com.example.preordering.constants.GeneralStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.NewServiceRequest;
import com.example.preordering.model.OrderTime;
import com.example.preordering.model.ServiceView;
import com.example.preordering.payload.ServiceRequest;
import com.example.preordering.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServiceRepository serviceRepository;
    private final UserAdminRepository userAdminRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserAdminService userAdminService;
    private final UserAdminSettingOfTimetableRepository userAdminSettingOfTimetableRepository;

    private static Logger logger = LoggerFactory.getLogger(ServicesService.class);



    public Service findServiceByCompanyIdAndServiceId(Long serviceId, Long companyId){
        return serviceRepository.findByServiceIdAndCompany_CompanyIdAndStatus(serviceId, companyId,GeneralStatuses.ACTIVE)
                .orElseThrow(() -> new BadRequestException("There is no such service"));
    }
    public List<Service> getAllServicesOfCategory(Long categoryId){
        return serviceRepository.getAllServicesByCategoryId(categoryId);
    }
    public void addServiceToCompany(ServiceRequest serviceRequest, Company company){

        List<Long> mastersId =
                userAdminRepository.getByUserAdminUsername(serviceRequest.getUsernameOfMasters());
        Service service = Service.builder()
                .company(company)
                .durationInMinutes(serviceRequest.getDurationInMinutes())
                .occupationName(serviceRequest.getTitle())
                .price(serviceRequest.getPrice())
                .employeesId(mastersId)
                .status(GeneralStatuses.ACTIVE)
                .build();

        serviceRepository.save(service);

        List<Long> servicesId = company.getServicesId();
        servicesId.add(service.getServiceId());
        company.setServicesId(servicesId);

        companyRepository.save(company);
    }
    public List<Long> getServicesIdOfCompany(Long companyId){
        return serviceRepository.findByCompanyId(companyId);
    }

    public boolean doesServiceWithThisTitleExist(String title, Long categoryId){
        return serviceRepository.existsByOccupationNameAndCompany_CompanyIdAndStatus(
                title, categoryId, GeneralStatuses.ACTIVE);
    }

    public List<OrderTime> availableTimeOfUserAdminOrMaster(String username,
                                                            Service service,
                                                           String date){
        LocalDate localDate;
        if(date == null){
            localDate = LocalDate.now();
        }else {
            localDate = LocalDate.parse(date);
        }
        UserAdmin userAdmin = userAdminRepository.findByUsername(username);
        if(userAdmin == null){
            throw new BadRequestException("there is no such master");
        }
        List<OrderTime> availableTimes = new ArrayList<>();
        UserAdminSettingsOfTimetable settingsOfTimetable =
                userAdminSettingOfTimetableRepository.findByusername(username);

        if(settingsOfTimetable.getWeekendDays() != null) {
            for (DayOfWeek day : settingsOfTimetable.getWeekendDays()) {
                if (day.equals(localDate.getDayOfWeek())) {
                    return availableTimes;
                }
            }
        }

        Long breakBetweenOrders =
                settingsOfTimetable.getBreakInMinutesBetweenOrders();
        int durationOfService =
                service.getDurationInMinutes();
        List<LocalTime> bookedStarts =
                userAdminTimeTableRepository.getStartsByDate(localDate, username);
        System.out.println("booked starts = " + bookedStarts);
        List<LocalTime> bookedEnd =
                userAdminTimeTableRepository.getEndsByDate(localDate, username);
        System.out.println("booked ends = " + bookedEnd);

        LocalTime startOfDay =
                settingsOfTimetable.getStart();
        LocalTime endOfDay =
                settingsOfTimetable.getFinish();
        LocalTime newStart = startOfDay;
        LocalTime newEnd =
                startOfDay.plus(durationOfService, ChronoUnit.MINUTES);

        if(settingsOfTimetable.getBreakFinish() == null && settingsOfTimetable.getBreakStart() == null
        && bookedStarts.isEmpty() && bookedEnd.isEmpty()){
            System.out.println(1);
            availableTimes.add(
                    new OrderTime(newStart, newEnd)
            );
            while (endOfDay.isAfter(newEnd)){
                newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
            }
        } else if (!bookedStarts.isEmpty() && !bookedEnd.isEmpty() &&
                settingsOfTimetable.getBreakStart() == null && settingsOfTimetable.getBreakFinish() == null) {
            if(!startOfDay.equals(bookedStarts.get(0)) && !endOfDay.equals(bookedEnd.get(0))) {
                System.out.println(2);
                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
                while (endOfDay.isAfter(newEnd)) {
                    newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                    newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
                    for (int i = 0; i < bookedStarts.size(); i++) {
                        if (!(ifTimesIntersect(bookedStarts.get(i), bookedEnd.get(i), newStart, newEnd))) {
                            availableTimes.add(
                                    new OrderTime(newStart, newEnd)
                            );
                        }
                    }
                }
            }
        } else if (settingsOfTimetable.getBreakStart() != null && settingsOfTimetable.getBreakFinish() != null
                    && bookedStarts.isEmpty() && bookedEnd.isEmpty()) {
            System.out.println(3);
            if(!ifTimesIntersect(settingsOfTimetable.getBreakStart(), settingsOfTimetable.getBreakFinish(),
                    newStart, newEnd)) {
                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
            }
            System.out.println("newStart = " + newStart + ", newEnd = " + newEnd + " 1");
            while (settingsOfTimetable.getBreakStart().isAfter(newEnd)){
                newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
                System.out.println("newStart = " + newStart + ", newEnd = " + newEnd);

                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
            }
            newStart = settingsOfTimetable.getBreakFinish();
            newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
            System.out.println("newStart = " + newStart + ", newEnd = " + newEnd + " 2");

            if(!ifTimesIntersect(endOfDay, endOfDay, newStart, newEnd)) {
                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
            }
            while (endOfDay.isAfter(newEnd)){
                newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
                System.out.println("newStart = " + newStart + ", newEnd = " + newEnd + " 3");
                availableTimes.add(
                        new OrderTime(newStart, newEnd)
                );
            }
        }
        else if(settingsOfTimetable.getBreakStart() != null && settingsOfTimetable.getBreakFinish() != null
                && !bookedStarts.isEmpty() && !bookedEnd.isEmpty()){
            System.out.println(4);
            for (int i = 0; i < bookedStarts.size(); i++){
                if(!(ifTimesIntersect(bookedStarts.get(i), bookedEnd.get(i), newStart, newEnd))){
                    availableTimes.add(
                            new OrderTime(newStart, newEnd)
                    );
                }
            }
            while (settingsOfTimetable.getBreakStart().isAfter(newEnd)){
                newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
                for (int i = 0; i < bookedStarts.size(); i++){
                    if(!(ifTimesIntersect(bookedStarts.get(i), bookedEnd.get(i), newStart, newEnd))){
                        availableTimes.add(
                                new OrderTime(newStart, newEnd)
                        );
                    }
                }
            }
            newStart = settingsOfTimetable.getBreakFinish();
            newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
            for (int i = 0; i < bookedStarts.size(); i++){
                if(!(ifTimesIntersect(bookedStarts.get(i), bookedEnd.get(i), newStart, newEnd))){
                    availableTimes.add(
                            new OrderTime(newStart, newEnd)
                    );
                }
            }
            while (endOfDay.isAfter(newEnd)){
                for (int i = 0; i < bookedStarts.size(); i++){
                    if(!(ifTimesIntersect(bookedStarts.get(i), bookedEnd.get(i), newStart, newEnd))){
                        availableTimes.add(
                                new OrderTime(newStart, newEnd)
                        );
                    }
                }
                newStart = newEnd.plus(breakBetweenOrders, ChronoUnit.MINUTES);
                newEnd = newStart.plus(durationOfService, ChronoUnit.MINUTES);
            }
        }
        return availableTimes;
    }

    public List<String> getMastersUsernames(List<Long> mastersId){
        return userAdminService.mastersUsernames(mastersId);
    }

    private Boolean ifTimesIntersect(LocalTime breakStart,
                                     LocalTime breakEnd,
                                     LocalTime newStart,
                                     LocalTime newEnd){
        return (breakStart.isAfter(newStart) && breakStart.isBefore(newEnd))
                ||
                (breakEnd.isAfter(newStart) && breakEnd.isBefore(newEnd))
                ||
                (breakStart.isBefore(newStart) && breakEnd.isAfter(newEnd))
                ||
                (breakStart.equals(newStart) && breakEnd.equals(newEnd));

    }
//    public boolean ifMasterHasSuchService(String occupationName, String username){
//        return serviceRepository.existsByOccupationNameAndUsernamesOfEmployees(
//                occupationName, List.of(username));
//    }

    @Transactional
    public void addService(NewServiceRequest serviceRequest, String username){

        if(companyService.ifThisMasterHasSoloCompany(username)){
            Company company =
                    companyRepository.getCompanyByDirectorUsername(username);

            logger.info("Company - {}", company.getCompanyName());

            if(doesServiceWithThisTitleExist(
                    serviceRequest.getServiceName(), company.getCompanyId())){
                throw new BadRequestException("You have already had this service!");
            }
            logger.info("Adding service!");
            saveServiceToCompanyToo(serviceRequest, company);
        }
        else {
            Category category =
                    categoryRepository.findByTitle(serviceRequest.getCategoryName());

            Location location = Location.builder()
                    .lat(serviceRequest.getLat())
                    .lon(serviceRequest.getLon())
                    .build();
            locationRepository.save(location);

            Company company = Company.builder()
                    .category(category)
                    .address(serviceRequest.getAddress())
                    .directorName(serviceRequest.getFullname())
                    .directorUsername(serviceRequest.getUsernameOfMaster())
                    .location(location)
                    .status(GeneralStatuses.ACTIVE)
                    .functionality(CompanyFunctionality.SOLO)
                    .mastersId(List.of(userAdminRepository.findUserByUsername(serviceRequest.getUsernameOfMaster())))
                    .build();

            logger.info("Company saved!");
            logger.info("Service is being added to company!");
            saveServiceToCompanyToo(serviceRequest, company);
        }
    }
    public void saveServiceToCompanyToo(NewServiceRequest serviceRequest, Company company){

        List<Long> mastersId =
                userAdminRepository.getByUserAdminUsername(List.of(serviceRequest.getUsernameOfMaster()));
        logger.info("MastersId = {}", mastersId);
        Service service =
                Service.builder()
                        .price(serviceRequest.getPrice())
                        .durationInMinutes(serviceRequest.getDurationInMinutes())
                        .company(company)
                        .employeesId(mastersId)
                        .occupationName(serviceRequest.getServiceName())
                        .status(GeneralStatuses.ACTIVE)
                        .build();
        logger.info("Services added!");

        serviceRepository.save(service);

        logger.info("Service id = {}", service.getServiceId());
        List<Long> servicesId;
        if(company.getServicesId() == null) {
             servicesId = Arrays.asList(service.getServiceId());
        }
        else {
            servicesId = company.getServicesId();
            servicesId.add(service.getServiceId());
        }
        logger.info("company = {}", company);
        logger.info("services id = {}", servicesId);

        company.setServicesId(servicesId);

        companyRepository.save(company);
//        companyRepository.saveServicesId(servicesId, company.getCompanyId());
    }
    public List<ServiceView> getServicesOfCompanies(Long categoryId, int page, int pageSize){


        Pageable recordsOnPage =
                PageRequest.of(page - 1, pageSize);

        List<ServiceView> serviceViews =
                new ArrayList<>();

        List<Company> companies =
                companyRepository.findAllByCategory_CategoryIdAndStatus
                        (categoryId, recordsOnPage, GeneralStatuses.ACTIVE);

        for (Company company: companies){
            List<String> servicesNames =
                    serviceRepository.findByServiceNamesIdInAndStatus(company.getServicesId(), GeneralStatuses.ACTIVE);

            serviceViews.add(
                    ServiceView.builder()
                            .servicesNames(servicesNames)
                            .companyImageName(company.getCompanyImageName())
                            .companyName(company.getCompanyName())
                            .companyUsername(company.getCompanyUsername())
                            .build()
            );
        }
        return serviceViews;
    }
}
