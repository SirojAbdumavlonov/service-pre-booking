package com.example.preordering.entity;

import com.example.preordering.entity.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order extends DateAudit {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private Long orderId;
    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime finish;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "client_id"
    )
    private Client client;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "service_id"
    )
    private Service services;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "userAdmin_id"
    )
    private UserAdmin userAdmin;
    @OneToOne(mappedBy = "order")
    private OrderStatus orderStatus;
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public void setFinish(LocalTime finish) {
        this.finish = finish;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Service getServices() {
        return services;
    }

    public void setServices(Service services) {
        this.services = services;
    }

    public UserAdmin getUserAdmins() {
        return userAdmin;
    }
    public void setUserAdmins(UserAdmin userAdmins) {
        this.userAdmin = userAdmins;
    }




}
