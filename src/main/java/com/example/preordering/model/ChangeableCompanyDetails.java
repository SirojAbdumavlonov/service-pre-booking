package com.example.preordering.model;

import java.util.ArrayList;
import java.util.List;

public class ChangeableCompanyDetails {
    public ChangeableCompanyDetails(){}
    private String companyName;

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    private String companyInfo;
    private String companyUsername;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String companyUsername) {
        this.companyUsername = companyUsername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getaLong() {
        return aLong;
    }

    public void setaLong(double aLong) {
        this.aLong = aLong;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public ChangeableCompanyDetails(String companyName, String directorUsername, String address, double aLong, double lat, List<String> phoneNumbers) {
        this.companyName = companyName;
        this.companyUsername = directorUsername;
        this.address = address;
        this.aLong = aLong;
        this.lat = lat;
        this.phoneNumbers = phoneNumbers;
    }

    private String address;

    private double aLong;
    private double lat;
    List<String> phoneNumbers = new ArrayList<>();
}
