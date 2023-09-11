package com.example.preordering.entity;

import com.example.preordering.entity.audit.TimeStampAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest extends TimeStampAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private String clientUsername;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }



    public String getRequestStatus() {
        return requestStatus;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String companyUsername) {
        this.companyUsername = companyUsername;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    String companyUsername;

    private String requestStatus;

    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
