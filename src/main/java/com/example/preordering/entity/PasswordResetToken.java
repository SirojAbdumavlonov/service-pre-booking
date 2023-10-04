//package com.example.preordering.entity;
//
//import jakarta.persistence.*;
//
//import java.util.Date;
//
//@Entity
//public class PasswordResetToken {
//
//    private static final int EXPIRATION = 60 * 24;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String token;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "userAdmin_id")
//    private UserAdmin userAdmin;
//
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "client_id")
//    private Client client;
//
//    private Date expiryDate;
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public UserAdmin getUserAdmin() {
//        return userAdmin;
//    }
//
//    public void setUserAdmin(UserAdmin userAdmin) {
//        this.userAdmin = userAdmin;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
//
//    public Date getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(Date expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//
//    public PasswordResetToken(String token, UserAdmin userAdmin, Client client) {
//        this.token = token;
//        this.userAdmin = userAdmin;
//        this.client = client;
//    }
//    public PasswordResetToken(){}
//}
