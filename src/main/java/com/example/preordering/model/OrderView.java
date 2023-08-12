package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderView {
    LocalTime start;
    LocalTime end;
    String clientUsername;
    String orderStatus;
    String serviceName;
    String companyName;
    Long orderId;
    LocalTime createdTime;

}
