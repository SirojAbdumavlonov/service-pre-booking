package com.example.preordering.model;

import java.util.List;

public class ChangeableUserDetails {
    public ChangeableUserDetails(){}
    public ChangeableUserDetails(String fullName, String username, List<String> phoneNumbers) {
        this.fullName = fullName;
        this.username = username;
        this.phoneNumbers = phoneNumbers;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    String fullName;

    String username;

    List<String> phoneNumbers;

}
