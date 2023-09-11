package com.example.preordering.payload;


import lombok.Builder;

@Builder
public class AuthenticationResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public AuthenticationResponse(String token) {
        this.token = token;
    }
    public AuthenticationResponse(){}

    private String token;

}
