package com.example.preordering.model;

import com.example.preordering.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyProfile {

    List<String> masters = new ArrayList<>();

    List<String> services = new ArrayList<>();

    List<String> phoneNumbers = new ArrayList<>();

    String companyName;
    String companyUsername;
    String address;
    String description;
    String directorName;
    String directorUsername;

    String imageOfCompany;

    double rate;
    Long successfulOrders;
    double lon;
    double lat;
    Long companyId;


}
