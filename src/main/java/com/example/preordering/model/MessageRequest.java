package com.example.preordering.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private String senderUsername;

    private String content;

    private Timestamp timestamp;

}
