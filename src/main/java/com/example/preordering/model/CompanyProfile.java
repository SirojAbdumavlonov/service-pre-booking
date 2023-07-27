package com.example.preordering.model;

import com.example.preordering.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyProfile {

    Set<String> masters = new HashSet<>();

    Set<Service> services = new HashSet<>();

    String companyName;
    String address;
    String description;
    String directorName;

    Long rate;
    Long successfulOrders;


}
