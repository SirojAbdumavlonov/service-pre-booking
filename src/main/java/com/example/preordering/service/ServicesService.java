package com.example.preordering.service;

import com.example.preordering.constants.CompanyFunctionality;
import com.example.preordering.constants.GeneralStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.NewServiceRequest;
import com.example.preordering.model.OrderTime;
import com.example.preordering.payload.ServiceRequest;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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


    public Service findServiceByCompanyIdAndServiceId(Long serviceId, Long companyId){
        return serviceRepository.findByServiceIdAndCompany_CompanyIdAndStatus(serviceId, companyId,GeneralStatuses.ACTIVE)
                .orElseThrow(() -> new BadRequestException("There is no such service"));
    }
    public List<Service> getAllServicesOfCategory(Long categoryId){
        return serviceRepository.getAllServicesByCategoryId(categoryId);
    }
    public void addServiceToCompany(ServiceRequest serviceRequest, Company company){
        Service service = Service.builder()
                .company(company)
                .durationInMinutes(serviceRequest.getDurationInMinutes())
                .occupationName(serviceRequest.getTitle())
                .price(serviceRequest.getPrice())
                .usernamesOfEmployees(serviceRequest.getUsernameOfMasters())
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
                userAdminRepository.findByUsername(username).getUserAdminSettingsOfTimetable();

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

    public void addService(NewServiceRequest serviceRequest, String username){

        if(companyService.ifThisMasterHasSoloCompany(username)){
            Company company =
                    companyRepository.getCompanyByDirectorUsername(username);
            if(doesServiceWithThisTitleExist(
                    serviceRequest.getServiceName(), company.getCompanyId())){
                throw new BadRequestException("You have already had this service!");
            }
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
                    .functionality(CompanyFunctionality.SOLO)
                    .mastersUsernames(List.of(serviceRequest.getUsernameOfMaster()))
                    .build();
            companyRepository.save(company);

            saveServiceToCompanyToo(serviceRequest, company);
        }
    }
    public void saveServiceToCompanyToo(NewServiceRequest serviceRequest, Company company){
        Service service =
                Service.builder()
                        .price(serviceRequest.getPrice())
                        .durationInMinutes(serviceRequest.getDurationInMinutes())
                        .company(company)
                        .usernamesOfEmployees(List.of(company.getDirectorUsername()))
                        .occupationName(serviceRequest.getServiceName())
                        .build();
        serviceRepository.save(service);

        List<Long> servicesId = company.getServicesId();
        servicesId.add(service.getServiceId());
        company.setServicesId(servicesId);

        companyRepository.save(company);
    }
}
