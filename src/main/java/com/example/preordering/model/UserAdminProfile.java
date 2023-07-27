package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminProfile {
    String username;
    Long totalSumOfOrders;
    Long countOfOrders;

}
