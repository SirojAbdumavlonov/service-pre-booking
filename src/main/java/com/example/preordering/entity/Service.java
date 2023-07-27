package com.example.preordering.entity;

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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "company_id"
    )
    private Company company;

    @ElementCollection
    List<String> usernames;

}
