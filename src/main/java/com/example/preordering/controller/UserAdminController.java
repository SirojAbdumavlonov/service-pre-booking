package com.example.preordering.controller;

import com.example.preordering.entity.*;
import com.example.preordering.model.*;
import com.example.preordering.payload.ApiResponse;
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
    private final ClientService clientService;
    private final CompanyService companyService;


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
    @GetMapping("/employee/zoir_employer")
    public ResponseEntity<?> zoirJonsPage(){
        String status = "all";
        LocalDate date = LocalDate.now();
        String username = "zoir_employer";
        if (userAdminService.getByUsername(username) instanceof UserAdmin userAdmin) {

            System.out.println("found");

            Long countOfSuccessfulOrders =
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
                            .imageName(userAdmin.getUserAdminImageName())
                            .countOfOrders(countOfSuccessfulOrders)
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
                            .details(userAdmin.getDetails())
                            .build();
            return ResponseEntity.ok(userAdminProfile);
        }
        return ResponseEntity.ok("not found");
    }
    @GetMapping("/employee/siroj_employer")
    public ResponseEntity<?> sirojJonsPage(){
        String status = "all";
        LocalDate date = LocalDate.now();
        String username = "siroj_employer";
        System.out.println("executed");
        if (userAdminService.getByUsername(username) instanceof UserAdmin userAdmin) {

            System.out.println("found");

            Long countOfSuccessfulOrders =
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
                            .imageName(userAdmin.getUserAdminImageName())
                            .countOfOrders(countOfSuccessfulOrders)
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
                            .details(userAdmin.getDetails())
                            .build();
            return ResponseEntity.ok(userAdminProfile);
        }
        return ResponseEntity.ok("not found");
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<?> mainPageOfUserAdminOrMaster(@PathVariable String username,
//                                                         @RequestParam(value = "date",required = false) LocalDate date,
//                                                         @RequestParam(value = "status",
//                                                                 required = false,defaultValue = "all") String status){
//        if (date == null){
//            date = LocalDate.now();
//        }
//        if (userAdminService.getByUsername(username) instanceof UserAdmin userAdmin) {
//            Long countOfSuccessfulOrders =
//                    userAdminService.countOfSuccessfulOrders(username, date);
//            Long sumOfExpectedOrders =
//                    userAdminService.totalSumOfSuccessfulOrders(username, date);
//            List<OrderTimeService> booked =
//                    userAdminService.getBookedTimes(date, username);
//            String companyName =
//                    userAdminService.getCompanyName(username);
//            List<String> occupations =
//                    userAdminService.getOccupationNames(companyName, username);
//            List<OrderView> orderViews =
//                    userAdminService.getAllOrders(date, username, status);
//            String statuss =
//                    userAdminService.getStatus(username);
//            UserAdminProfile userAdminProfile =
//                    UserAdminProfile.builder()
//                            .countOfOrders(countOfSuccessfulOrders)
//                            .totalSumOfOrders(sumOfExpectedOrders)
//                            .username(username)
//                            .booked(booked)
//                            .firstname(userAdmin.getFirstName())
//                            .lastname(userAdmin.getLastName())
//                            .phonenumber(userAdmin.getPhoneNumber())
//                            .companyName(companyName)
//                            .occupations(occupations)
//                            .orders(orderViews)
//                            .status(statuss)
//                            .date(date)
//                            .build();
//            return ResponseEntity.ok(userAdminProfile);
//        }
//        if(userAdminService.getByUsername(username) instanceof Client client) {
//            status = "ACCEPTED";
//            Long reports = clientService.getClientReports(username);
//            String status1 =
//                    clientService.getClientStatus(username);
//            List<OrderView> upcomingOrders =
//                    userAdminService.getAllOrders(date, username, status);
//            ClientProfile clientProfile =
//                    ClientProfile.builder()
//    .details(client.getDetails())
//                            .imageName(client.getClientImageName())
//                            .firstname(client.getFirstName())
//                            .lastName(client.getLastName())
//                            .phoneNumbers(client.getPhoneNumber())
//                            .reports(reports)
//                            .status(status1)
//                            .username(client.getUsername())
//                            .date(date)
//                            .upcomingOrders(upcomingOrders)
//                            .build();
//            return ResponseEntity.ok(clientProfile);
//        }
//        return null;
//    }
    @PutMapping("/{username}")
    public ResponseEntity<?> acceptOrDeclineAnOrder(@PathVariable String username,
                                                    @RequestParam(value = "clusr") String clientUsername,
                                                    @RequestParam(value = "typopt") String option,
                                                    @RequestParam(value = "idord") Long orderId){

        Order order = userAdminService.getOrderByClientUsernameAndOrderId(clientUsername,  orderId);

        if(option.equals("decline")){
            userAdminService.changeStatusToDeclined(orderId);
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
    @GetMapping("/{username}/company")
    public ResponseEntity<?> getCompanyOfUserAdmin(@PathVariable String username){

        Company company =
                companyService.getCompanyByDirectorUsername(username);

        if(company == null){
            return ResponseEntity.ok(new ApiResponse("You don't have any company"));
        }

        return ResponseEntity.ok(company);
    }
    @GetMapping("/{username}/offers")
    public ResponseEntity<?> getRequestsForJoining(@PathVariable String username){
        List<JoinRequest> joinRequests =
                companyService.getJoinRequest(username);
        return ResponseEntity.ok(joinRequests);
    }

}
