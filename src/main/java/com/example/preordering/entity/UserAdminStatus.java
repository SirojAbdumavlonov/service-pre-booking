package com.example.preordering.entity;

import com.example.preordering.constants.UserAdminStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAdminStatus {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "status_id")
    private Long statusId;

    private int adminStatus = UserAdminStatuses.VERY_GOOD;

    Long rate;

    Long reports;



}
