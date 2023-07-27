package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientProfile {
    String username;
    String firstname;
    String secondname;
    String email;
    String phoneNumber;
    String status;
    int reports;
//reports and news for clients and admins
// active orders of current date and total sum
    //loyal, monitoring, finance, total clients, compare to other dates;
    //
//useradmin's sotrudniki
}
