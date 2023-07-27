package com.example.preordering.entity;

import com.example.preordering.constants.UserAdminStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminStatus {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long statusId;

    @JoinColumn(
            name = "userAdmin_id"
    )
    @OneToOne(
            cascade = CascadeType.ALL
    )
    UserAdmin userAdmin;

    private int adminStatus = UserAdminStatuses.VERY_GOOD;

    Long rate;

    Long reports;



}