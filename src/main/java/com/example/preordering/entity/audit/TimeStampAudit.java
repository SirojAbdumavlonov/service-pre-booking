package com.example.preordering.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdTime"},
        allowGetters = true
)
public abstract class TimeStampAudit implements Serializable {

    @CreationTimestamp
    private LocalDateTime createdTime;

    public LocalDateTime getCreatedDate() {
        return createdTime;
    }

    public void setCreatedDate(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }


}
