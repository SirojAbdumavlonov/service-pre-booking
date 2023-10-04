package com.example.preordering.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CompanyFilling {

    @NotBlank
    private String companyName;

    @NotBlank
    private String directorName;

    @Size(max = 100)
    private String description;

    private String address;

    double lon;

    double lat;

    String directorUsername;

    String companyUsername;

    List<String> phoneNumbers = new ArrayList<>();

}
