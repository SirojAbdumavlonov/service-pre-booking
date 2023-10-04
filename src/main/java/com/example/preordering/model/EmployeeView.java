package com.example.preordering.model;

import java.util.List;

public class EmployeeView {
    public String getImageOfEmployee() {
        return imageOfEmployee;
    }

    public void setImageOfEmployee(String imageOfEmployee) {
        this.imageOfEmployee = imageOfEmployee;
    }

    public String getUsernameOfEmployee() {
        return usernameOfEmployee;
    }

    public void setUsernameOfEmployee(String usernameOfEmployee) {
        this.usernameOfEmployee = usernameOfEmployee;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<String> getServicesNames() {
        return servicesNames;
    }

    public void setServicesNames(List<String> servicesNames) {
        this.servicesNames = servicesNames;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    String imageOfEmployee;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    String role;
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public EmployeeView(){}

    public EmployeeView(String role, String imageOfEmployee, String usernameOfEmployee, String companyName, List<String> servicesNames, String location, Long rate, String details) {
        this.imageOfEmployee = imageOfEmployee;
        this.usernameOfEmployee = usernameOfEmployee;
        this.companyName = companyName;
        this.servicesNames = servicesNames;
        this.location = location;
        this.rate = rate;
        this.role = role;
        this.details = details;
    }

    String usernameOfEmployee;

    String companyName;

    List<String> servicesNames;

    String location;

    Long rate;

}
