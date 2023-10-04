package com.example.preordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "conversation_id"
    )
    private Conversations conversations;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "userAdmin_id"
    )
    private UserAdmin sender;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "client_id"
    )
    private UserAdmin recipient;

    private String content;
    private Timestamp timestamp;





}
