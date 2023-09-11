package com.example.preordering.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @JsonFormat(pattern = "hh:mm:ss")
    private String start;

    @JsonFormat(pattern = "hh:mm:ss")
    private String finish;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private String date;

    private String username;

    String fullName;

    String phoneNumber;

    String services;

    Long totalSum;

    Long totalTimeInMinutes;
}
