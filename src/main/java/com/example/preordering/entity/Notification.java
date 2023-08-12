package com.example.preordering.entity;

import com.example.preordering.entity.audit.DateAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Entity
@Builder
@AllArgsConstructor
public class Notification extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String context;
    private int userRole;


    public Notification(){}

}
