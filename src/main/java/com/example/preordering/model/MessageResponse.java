package com.example.preordering.model;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class MessageResponse {
    private String content;
    private Timestamp timestamp;
    private String senderUsername;

    public MessageResponse(String content, Timestamp timestamp, String senderUsername) {
        this.content = content;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
}
