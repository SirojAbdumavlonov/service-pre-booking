package com.example.preordering.entity;

import com.example.preordering.constants.ClientStatuses;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientsStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;
    @OneToOne
    @JoinColumn(
            name = "client_id"
    )
    private Client client;

    private int status = ClientStatuses.LOYAL;

    private int reports = 0;

}
