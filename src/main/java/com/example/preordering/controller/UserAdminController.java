package com.example.preordering.controller;

import com.example.preordering.entity.*;
import com.example.preordering.model.*;
import com.example.preordering.service.ClientService;
import com.example.preordering.service.CompanyService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;
    private final CompanyService companyService;
    private final ClientService clientService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        userAdminService.deleteUser(username);

        return ResponseEntity.ok("Deleted successfully!");
    }

    @GetMapping("/categories/{categoryId}/employees")
    public ResponseEntity<?> getEmployeesOfCategory(@PathVariable Long categoryId){

        List<EmployeeView> getEmployees =
                userAdminService.getEmployees(categoryId);

        return ResponseEntity.ok(getEmployees);
    }


    @GetMapping("/{username}")
    public ResponseEntity<?> mainPageOfUserAdminOrMaster(@PathVariable String username,
                                                         @RequestParam(value = "date",required = false) LocalDate date,
                                                         @RequestParam(value = "status",
                                                                 required = false,defaultValue = "all") String status){
        if (date == null){
            date = LocalDate.now();
        }
        UserAdmin userAdmin =
                userAdminService.getByUsername(username);
        //EMPLOYER OR EMPLOYEE PAGE WITH REQUESTED ORDERS
        if ((userAdmin.getRole().equals("EMPLOYEE") || userAdmin.getRole().equals("EMPLOYER")) && status.equals("all")) {
            Long countOfSuccessfulOrders =
                    userAdminService.countOfSuccessfulOrders(username, date);
            Long sumOfExpectedOrders =
                    userAdminService.totalSumOfSuccessfulOrders(username, date);
            List<OrderTimeService> booked =
                    userAdminService.getBookedTimes(date, username);
            String companyName =
                    userAdminService.getCompanyName(username);
            Company company =
                    companyService.getCompanyByDirectorUsername(username);
            List<String> occupations =
                    userAdminService.getOccupationNames(companyName, username);
            List<OrderView> orderViews =
                    userAdminService.getAllOrders(date, username, status);
            String statuss =
                    userAdminService.getStatus(username);
            UserAdminProfile userAdminProfile =
                    UserAdminProfile.builder()
                            .id(userAdmin.getUserAdminId())
                            .countOfOrders(countOfSuccessfulOrders)
                            .totalSumOfOrders(sumOfExpectedOrders)
                            .username(username)
                            .booked(booked)
                            .firstname(userAdmin.getFirstName())
                            .lastname(userAdmin.getLastName())
                            .phoneNumbers(userAdmin.getPhoneNumber())
                            .name(companyName)
                            .occupations(occupations)
                            .orders(orderViews)
                            .status(statuss)
                            .date(date)
                            .description(userAdmin.getDetails())
                            .image(userAdmin.getUserAdminImageName())
                            .address(company.getAddress())
                            .location(company.getLocation())
                            .build();
            return ResponseEntity.ok(userAdminProfile);
        }
        //ACCEPTED ORDERS PAGE
        else if((userAdmin.getRole().equals("EMPLOYEE") || userAdmin.getRole().equals("EMPLOYER")) && status.equals("accepted")){

            List<OrderView> upcomingOrders =
                    userAdminService.getAllOrders(date, username, status.toUpperCase());
            return ResponseEntity.ok(upcomingOrders);

        }

        //CLIENT PAGE WITH REQUESTED ORDERS
        else{
            Long reports = clientService.getClientReports(username);
            String status1 =
                    clientService.getClientStatus(username);
            List<OrderView> upcomingOrders =
                    userAdminService.getAllOrders(date, username, status.toUpperCase());
            ClientProfile clientProfile =
                    ClientProfile.builder()
                            .details(userAdmin.getDetails())
                            .imageName(userAdmin.getUserAdminImageName())
                            .firstname(userAdmin.getFirstName())
                            .lastName(userAdmin.getLastName())
                            .phoneNumbers(userAdmin.getPhoneNumber())
                            .reports(reports)
                            .status(status1)
                            .username(userAdmin.getUsername())
                            .date(date)
                            .upcomingOrders(upcomingOrders)
                            .clientId(userAdmin.getUserAdminId())
                            .build();
            return ResponseEntity.ok(clientProfile);
        }
    }

    //THIS IS EMPLOYEE'S PAGE
    @PutMapping("/order/{orderId}")
    public ResponseEntity<?> acceptOrDeclineOrPostponeAnOrder(@RequestParam(value = "typopt") String option,
                                                              @PathVariable Long orderId,
                                                              @RequestParam(name = "date", required = false) String date,
                                                              @RequestParam(name = "newstrt", required = false) String newStartTime,
                                                              @RequestParam(name = "newend", required = false) String newEndTime,
                                                              @RequestParam(name = "usernm") String username){

        Order order = userAdminService.getOrderByOrderId(orderId);

        if(option.equals("decline")){
            userAdminService.changeStatusToDeclined(orderId);
        }
        else if(option.equals("postpone")){
            userAdminService.postpone(order, date, newStartTime, newEndTime, username);
        }
        else {
            userAdminService.changeStatusToAccepted(order);
        }
        return null;
    }
    @PutMapping("/{username}/details")
    public ResponseEntity<?> changeUserDetails(@PathVariable String username,
                                               @RequestBody ChangeableUserDetails details){

        userAdminService.updateUserDetails(username, details);

        return ResponseEntity.ok("Changed successfully");
    }
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE','ROLE_EMPLOYER','SUPER_ADMIN')")
    @GetMapping("/{username}/timetable-settings")
    public ResponseEntity<?> getDefaultSettingsOfTimeTableOfUserAdmin(@PathVariable String username){

        UserAdminSettingsOfTimetable userAdminSettingsOfTimetable =
                userAdminService.getSettingsOfTimetable(username);

        return ResponseEntity.ok(userAdminSettingsOfTimetable);
    }

    @GetMapping("/{username}/offers")
    public ResponseEntity<?> getRequestsForJoining(@PathVariable String username){
        List<JoinRequest> joinRequests =
                companyService.getJoinRequest(username);
        return ResponseEntity.ok(joinRequests);
    }

    @GetMapping("/validation")
    public ResponseEntity<?> isUsernameUnique(@RequestParam("username") String username,
                                              @RequestParam("opt") String option){

        if(option.equals("company")){
            companyService.checkUsername(username);
        }
        else if(option.equals("user")) {
            userAdminService.checkUsername(username);
        }

        return ResponseEntity.ok("Ok!");
    }


}
