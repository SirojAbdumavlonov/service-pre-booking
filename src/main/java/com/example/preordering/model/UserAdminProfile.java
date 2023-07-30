package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminProfile {
    String username;
    Long totalSumOfOrders;
    Long countOfOrders;

    String firstname;
    String lastname;
    String phonenumber;
    String companyName;

    String status;
    double rate;
    LocalDate date;

    List<String> occupations = new ArrayList<>();
    List<OrderTimeService> booked = new ArrayList<>();
    List<OrderView> orders = new ArrayList<>();
    List<LocalDate> dates = new ArrayList<>();
}
