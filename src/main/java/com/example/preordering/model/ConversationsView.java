package com.example.preordering.model;

import com.example.preordering.entity.Conversations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversationsView {
    private Conversations conversations;
    private String lastMessage;
    private Timestamp timestamp;
}
