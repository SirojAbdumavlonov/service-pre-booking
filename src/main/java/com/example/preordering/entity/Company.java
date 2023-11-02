package com.example.preordering.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "company_id")
    private Long id;

    private String name;

    String username;

    private String directorName;

    private String directorUsername;

    @Size(max = 100)
    private String description;

    private String address;

    @OneToOne(
        fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "location_id"
    )

    private Location location;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "category_id"
    )
    private Category category;
    private String image;

    @ElementCollection
    private List<Long> mastersId = new ArrayList<>();

    @ElementCollection
    private List<Long> servicesId = new ArrayList<>();

    private String functionality;

    @ElementCollection
    private List<String> phoneNumbers = new ArrayList<>();

    private String status;
}