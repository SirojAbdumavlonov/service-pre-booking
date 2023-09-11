package com.example.preordering.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminTimetable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @OneToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id"
    )
    private Order order;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "client_id"
    )
    private Client client;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "userAdmin_id"
    )
    private UserAdmin userAdmin;

    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime finish;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    private boolean employeeOrClientStatus;

}
