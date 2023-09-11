package com.example.preordering.model;

import lombok.Builder;

@Builder
public class ProfileDetails {
    String role;
    String username;

    public ProfileDetails(String role, String username, String imageName, String fullName) {
        this.role = role;
        this.username = username;
        this.imageName = imageName;
        this.fullName = fullName;
    }
    public ProfileDetails(){}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String imageName;

    String fullName;
}
