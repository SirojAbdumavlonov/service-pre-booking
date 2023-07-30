package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProfile {
    private Long price;

    private String occupationName;

    List<OrderTime> orderTimes;

    List<String> mastersUsernames;

    List<LocalDate> dates;

    String chosenDate;
    String chosenMaster;
}
