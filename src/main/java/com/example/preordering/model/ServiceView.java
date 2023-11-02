package com.example.preordering.model;

import lombok.Builder;

import java.util.List;

@Builder
public class ServiceView {
    String companyName;
    String companyUsername;
    String companyImageName;
    Long price;
    public ServiceView(String companyName, String companyImageName, String companyUsername, Long price){
        this.companyImageName = companyImageName;
        this.companyName = companyName;
        this.companyUsername = companyUsername;
        this.price = price;
    }
    public ServiceView(){}
}
