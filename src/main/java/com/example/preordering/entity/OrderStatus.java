package com.example.preordering.entity;

import com.example.preordering.constants.OrderStatuses;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatus {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long orderStatusId;
    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "order_id"
    )

    private Order order;

    private int employeeResponseStatus = OrderStatuses.WAITING;

    private int clientResponseStatus = OrderStatuses.WAITING;

    private double rate;

}
