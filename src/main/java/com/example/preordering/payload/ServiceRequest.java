package com.example.preordering.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {

    String title;

    Long price;

    int durationInMinutes;

    List<String> usernameOfMasters;

    String categoryName;

}
