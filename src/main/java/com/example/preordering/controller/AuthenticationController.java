package com.example.preordering.controller;

import com.example.preordering.exception.BadRequestException;
import com.example.preordering.payload.ApiResponse;
import com.example.preordering.payload.AuthenticationRequest;
import com.example.preordering.payload.AuthenticationResponse;
import com.example.preordering.payload.RegisterRequest;
import com.example.preordering.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;
;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> registerUser(
           @RequestBody @Valid RegisterRequest request
    ){

        if(service.ifUsernameExists(request.getUsername())){
            throw new BadRequestException("Username is already taken!");

        }

        if (service.isUserRegistered(request.getEmail(), request.getRole())){
            throw new BadRequestException("Email address already in use!");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{username}")
                .buildAndExpand(request.getUsername()).toUri();

        AuthenticationResponse authenticationResponse = service.registerUser(request);
//        return ResponseEntity.created(location).body(new ApiResponse("You are registered successfully"));
        return ResponseEntity.ok(authenticationResponse);
    }
//    @GetMapping("/signup")
//    public ResponseEntity<?> getSignUpPage(Model model){
//        model.addAttribute("register", new RegisterRequest());
//
//        return ResponseEntity;
//    }
//    @GetMapping("/signin")
//    public String getSignIn(Model model){
//
//            model.addAttribute("auth", new RegisterRequest());
//            return "pages/auth/signin";
//
//    }
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @ModelAttribute("auth") @Valid AuthenticationRequest request
    ){
        AuthenticationResponse authenticationResponse = service.authenticateUserAdmin(request);
//        return ResponseEntity.ok(new ApiResponse("You are logged in successfully!"));
        return ResponseEntity.ok(authenticationResponse);

    }

}