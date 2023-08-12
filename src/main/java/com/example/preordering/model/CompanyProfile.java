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

    List<Service> services = new ArrayList<>();

    String companyName;
    String address;
    String description;
    String directorName;
    String imageOfCompany;

    double rate;
    Long successfulOrders;


}
