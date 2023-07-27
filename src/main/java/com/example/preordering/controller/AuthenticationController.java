package com.example.preordering.controller;

import com.example.preordering.exception.BadRequestException;
import com.example.preordering.payload.AuthenticationRequest;
import com.example.preordering.payload.AuthenticationResponse;
import com.example.preordering.payload.RegisterRequest;
import com.example.preordering.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;
;

    @PostMapping("/client/register")
    public ResponseEntity<AuthenticationResponse> registerClient(
           @RequestBody RegisterRequest request
    ){

        if(service.ifUsernameExists(request.getUsername())){
            throw new BadRequestException("Username is already taken!");
        }

        if (service.isClientRegistered(request.getEmail())){
            throw new BadRequestException("Email address already in use!");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{username}")
                .buildAndExpand(request.getUsername()).toUri();


        return ResponseEntity.created(location).body(service.registerClient(request));
    }
    @PostMapping("/client/signin")
    public ResponseEntity<AuthenticationResponse> authenticateClient(
            @RequestBody AuthenticationRequest request
    ){
        if(!service.isClientRegistered(request.getEmail())){
            throw new BadRequestException("Client with this email is not registered");
        }


        return ResponseEntity.ok(service.authenticateClient(request));

    }
    @PostMapping({"/admin/register","/master/register"})
    public ResponseEntity<AuthenticationResponse> registerUserAdmin(
            @RequestBody RegisterRequest request,
            @NonNull HttpServletRequest httpServletRequest

    ){
        String role = "admin";

        if(httpServletRequest.getServletPath().contains("master")) {
            role = "master";
        }
        if(service.ifUsernameExists(request.getUsername())){
            throw new BadRequestException("Username is already taken");
        }
        if(service.isUserAdminRegistered(request.getEmail())){
            throw new BadRequestException("Email address already in use!");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{username}")
                .buildAndExpand(request.getUsername()).toUri();

        return ResponseEntity.created(location).body(service.registerUserAdmin(request, role));
    }
    @PostMapping({"/admin/signin","/master/signin"})
    public ResponseEntity<AuthenticationResponse> authenticateUserAdmin(
            @RequestBody AuthenticationRequest request
    ){

        if(!service.isUserAdminRegistered(request.getEmail())){
            throw new BadRequestException("Admin with this email is not registered");
        }

        return ResponseEntity.ok(service.authenticateUserAdmin(request));

    }

}