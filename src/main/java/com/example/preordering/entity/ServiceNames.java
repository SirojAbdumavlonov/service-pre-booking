package com.example.preordering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ServiceNames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceNameId;

    String serviceName;
}
