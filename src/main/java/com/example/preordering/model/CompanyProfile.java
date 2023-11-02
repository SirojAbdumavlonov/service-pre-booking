package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyProfile {

    List<String> masters = new ArrayList<>();

    List<String> services = new ArrayList<>();

    List<String> phoneNumbers = new ArrayList<>();

    String name;
    String username;
    String address;
    String description;
    String directorName;
    String directorUsername;

    String image;

    double rate;
    Long successfulOrders;
    double lon;
    double lat;
    Long id;


}
