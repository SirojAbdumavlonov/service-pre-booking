package com.example.preordering.model;

import lombok.Builder;

@Builder
public class NewServiceRequest {
    public NewServiceRequest(){}
    public NewServiceRequest(String serviceName, Long price, int durationInMinutes, double lon, double lat, String address, String fullname, String categoryName, String usernameOfMaster) {
        this.serviceName = serviceName;
        this.price = price;
        this.durationInMinutes = durationInMinutes;
        this.lon = lon;
        this.lat = lat;
        this.address = address;
        this.fullname = fullname;
        this.categoryName = categoryName;
        this.usernameOfMaster = usernameOfMaster;
    }

    String serviceName;

    Long price;

    int durationInMinutes;

    double lon;

    double lat;

    String address;

    String fullname;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUsernameOfMaster() {
        return usernameOfMaster;
    }

    public void setUsernameOfMaster(String usernameOfMaster) {
        this.usernameOfMaster = usernameOfMaster;
    }

    String categoryName;

    String usernameOfMaster;

}
