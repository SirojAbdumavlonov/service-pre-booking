package com.example.preordering.entity;

import com.example.preordering.constants.OrderStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private int orderStatus = OrderStatuses.WAITING;

    private int rate;

}
