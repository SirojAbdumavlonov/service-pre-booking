package com.example.preordering.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


public class DeviceToken {
    @OneToOne
    @JoinColumn(
            name = "user_device_id"
    )
    private UserAdmin userAdmin;
    private String token;
}
