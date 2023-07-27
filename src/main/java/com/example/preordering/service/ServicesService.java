package com.example.preordering.service;

import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.OrderTime;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.ServiceRequest;
import com.example.preordering.repository.ServiceRepository;
import com.example.preordering.repository.UserAdminDefaultTimetableRepository;
import com.example.preordering.repository.UserAdminRepository;
import com.example.preordering.repository.UserAdminTimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;

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
    private final UserAdminDefaultTimetableRepository userAdminDefaultTimetableRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    public Service findServiceByCompanyIdAndServiceId(Long serviceId, Long companyId){
        return serviceRepository.findByServiceIdAndCompany_CompanyId(serviceId, companyId)
                .orElseThrow(() -> new BadRequestException("There is no such service"));
    }
    public void addServiceToCompany(ServiceRequest serviceRequest, Company company){
        Service service = Service.builder()
                .company(company)
                .durationInMinutes(serviceRequest.getDurationInMinutes())
                .occupationName(serviceRequest.getTitle())
                .price(serviceRequest.getPrice())
                .usernames(serviceRequest.getUsernameOfMasters())
                .build();
        serviceRepository.save(service);
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
                userAdminDefaultTimetableRepository.findByUserAdminUsername(username);
        if(!settingsOfTimetable.getWorkDay()){
            return availableTimes;
        }
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
}
