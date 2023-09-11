package com.example.preordering.model;

public class SavedPassword {
    String token;

    String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SavedPassword(String token, String password) {
        this.token = token;
        this.password = password;
    }
    public SavedPassword(){}
}
