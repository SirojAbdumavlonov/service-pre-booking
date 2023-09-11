package com.example.preordering.model;

public class EmployeeSearched {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmployeeImageName() {
        return employeeImageName;
    }

    public void setEmployeeImageName(String employeeImageName) {
        this.employeeImageName = employeeImageName;
    }

    public EmployeeSearched(String username, String fullname, String employeeImageName) {
        this.username = username;
        this.fullname = fullname;
        this.employeeImageName = employeeImageName;
    }

    String username;

    String fullname;

    String employeeImageName;
    public EmployeeSearched(){}
}
