package com.example.preordering.service;

import com.example.preordering.entity.Order;
import com.example.preordering.entity.TemporaryOrdersTime;
import com.example.preordering.entity.UserAdminTimetable;
import com.example.preordering.repository.TemporaryOrdersRepository;
import com.example.preordering.repository.UserAdminTimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final TemporaryOrdersRepository temporaryOrdersRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    @Async
    @Scheduled(fixedRate = 43_200_000)
    public void checkTableOfNotifications(){
        temporaryOrdersRepository.deleteAll();
        List<UserAdminTimetable> orderWithin12Hours =
                userAdminTimeTableRepository.getOrdersWithin12Hours
                        (LocalDate.now(), LocalTime.now(), LocalDate.now().plusDays(1), LocalTime.now().plusHours(12));
        for(UserAdminTimetable order : orderWithin12Hours){
            temporaryOrdersRepository.save(TemporaryOrdersTime
                    .builder()
                            .companyUsername(order.getOrder().getServices().getCompany().getCompanyUsername())
                            .occupationName(order.getOrder().getServices().getOccupationName())
                            .date(order.getDate())
                            .fullName(order.getClient().getFirstName() + " " + order.getClient().getLastName())
                            .time(order.getStart())
                    .build()
            );
        }
    }
    @Async
    @Scheduled(cron = "0 15 * * * *")
    public void checkNotificationAndSend(){

    }



}
