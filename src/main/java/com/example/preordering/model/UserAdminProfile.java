package com.example.preordering.model;

import com.example.preordering.entity.Location;
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
    Long id;
    String username;
    String image;
    String description;

    Long totalSumOfOrders;
    Long countOfOrders;

    String firstname;
    String lastname;
    List<String> phoneNumbers;
    String name;

    Location location;
    String address;

    String status;
    double rate;
    LocalDate date;

    List<String> occupations = new ArrayList<>();
    List<OrderTimeService> booked = new ArrayList<>();
    List<OrderView> orders = new ArrayList<>();
}
