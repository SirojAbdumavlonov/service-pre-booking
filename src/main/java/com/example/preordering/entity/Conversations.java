package com.example.preordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversations {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "conversation_id"
    )
    private Long conversationId;

    @ManyToOne(
          fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "sender"
    )
    private UserAdmin sender;
    @ManyToOne(
         fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "recipient"
    )
    private UserAdmin recipient;
}
