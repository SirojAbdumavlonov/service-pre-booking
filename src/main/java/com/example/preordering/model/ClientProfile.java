package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfile {
    Long clientId;
    String imageName;
    String username;
    String firstname;
    String lastName;
    List<String> phoneNumbers;
    String details;

    String status;
    Long reports;

    LocalDate date;
    List<OrderView> upcomingOrders;
    String orders;

//reports and news for clients and admins
// active orders of current date and total sum
    //loyal, monitoring, finance, total clients, compare to other dates;
    //
//useradmin's sotrudniki
}
