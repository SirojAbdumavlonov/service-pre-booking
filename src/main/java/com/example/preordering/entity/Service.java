package com.example.preordering.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    private long price;
    private int durationInMinutes;
    private String occupationName;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "company_id"
    )
    private Company company;

    @ElementCollection
    List<String> usernamesOfEmployees;

}
