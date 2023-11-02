package com.example.preordering.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmployeeView {

    String image;


    String role;
    String description;

    String username;

    String name;

    List<String> services;

    String location;

    Long rate;

}
