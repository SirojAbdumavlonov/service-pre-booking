package com.example.preordering.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

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

    private Long companyId;

    @NotBlank
    private String companyName;

    @NotBlank
    private String directorName;

    @NotBlank
    private String directorUsername;

    @Size(max = 100)
    private String description;
    private String address;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "category_id"
    )
    private Category category;

    private String companyImageName;

    @ElementCollection
    private List<Long> mastersId = new ArrayList<>();

    @ElementCollection
    private List<Long> servicesId = new ArrayList<>();


}
