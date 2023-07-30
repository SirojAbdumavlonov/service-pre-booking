package com.example.preordering.controller;

import com.example.preordering.entity.Order;
import com.example.preordering.entity.UserAdmin;
import com.example.preordering.model.OrderTime;
import com.example.preordering.model.OrderTimeService;
import com.example.preordering.model.OrderView;
import com.example.preordering.model.UserAdminProfile;
import com.example.preordering.service.UserAdminService;
import com.example.preordering.utils.DaysGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;
    @GetMapping("/{username}")
    public ResponseEntity<?> mainPageOfUserAdminOrMaster(@PathVariable String username,
                                                         @RequestParam(value = "date",required = false)
                                                         LocalDate date,
                                                         @RequestParam(value = "status",required = false,defaultValue = "WAITING")
                                                             String status){

        if (date == null){
            date = LocalDate.now();
        }
        if (userAdminService.getByUsername(username) instanceof UserAdmin userAdmin) {
            Long countOdExpectedOrders =
                    userAdminService.countOfSuccessfulOrders(username, date);
            Long sumOfExpectedOrders =
                    userAdminService.totalSumOfSuccessfulOrders(username, date);
            List<OrderTimeService> booked =
                    userAdminService.getBookedTimes(date, username);
            String companyName =
                    userAdminService.getCompanyName(username);
            List<String> occupations =
                    userAdminService.getOccupationNames(companyName, username);
            List<OrderView> orderViews =
                    userAdminService.getAllOrders(date, username, status);
            String statuss =
                    userAdminService.getStatus(username);
            UserAdminProfile userAdminProfile =
                    UserAdminProfile.builder()
                            .countOfOrders(countOdExpectedOrders)
                            .totalSumOfOrders(sumOfExpectedOrders)
                            .username(username)
                            .booked(booked)
                            .firstname(userAdmin.getFirstName())
                            .lastname(userAdmin.getLastName())
                            .phonenumber(userAdmin.getPhoneNumber())
                            .companyName(companyName)
                            .occupations(occupations)
                            .orders(orderViews)
                            .status(statuss)
                            .date(date)
                            .dates(DaysGenerator.get7Days())
                            .build();
            return ResponseEntity.ok(userAdminProfile);
        }
        return  null;
    }
    @PutMapping("/{username}")
    public ResponseEntity<?> acceptOrDeclineAnOrder(@PathVariable String username,
                                                    @RequestParam(value = "clusr") String clientUsername,
                                                    @RequestParam(value = "typopt") String option,
                                                    @RequestParam(value = "idord") Long orderId){
        Order order = userAdminService.getOrderByClientUsernameAndOrderId(clientUsername,  orderId);
        if(option.equals("decline")){
            userAdminService.changeStatusToDeclined(orderId);
        }
        userAdminService.changeStatusToAccepted(order);

        return null;
    }


}
