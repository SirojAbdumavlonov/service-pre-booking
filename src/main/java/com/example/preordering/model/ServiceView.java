package com.example.preordering.model;

import lombok.Builder;

import java.util.List;

@Builder
public class ServiceView {
    List<String> servicesNames;
    String companyName;
    String companyUsername;
    String companyImageName;

    Long price;
}
