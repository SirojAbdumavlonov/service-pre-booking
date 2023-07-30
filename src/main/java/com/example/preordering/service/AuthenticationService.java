package com.example.preordering.service;


import com.example.preordering.constants.DefaultSettingsOfUserAdminTimetable;
import com.example.preordering.constants.UserAdminStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.payload.AuthenticationRequest;
import com.example.preordering.payload.AuthenticationResponse;
import com.example.preordering.payload.RegisterRequest;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final ClientsStatusRepository clientsStatusRepository;
    private final UserAdminSettingsOfTimetableRepository userAdminSettingsOfTimetableRepository;
    private final UserAdminStatusRepository userAdminStatusRepository;

    public Boolean ifUsernameExists(String username){
        return clientRepository.existsByUsername(username) || userAdminRepository.existsByUsername(username);
    }

    private String validPhoneNumber(String phoneNumber){
        if(phoneNumber.startsWith("+998") && phoneNumber.length() == 13){
            return phoneNumber;
        }
        throw new BadRequestException("invalid telephone number");
    }
    private String validUsername(String username){
        if(clientRepository.existsByUsername(username) || userAdminRepository.existsByUsername(username))
        {
            throw new BadRequestException("this username has been taken");
        }
        return username;
    }
    public Boolean isClientRegistered(String email){
        return clientRepository.existsByEmail(email);
    }
    public Boolean isUserAdminRegistered(String email){
        return userAdminRepository.existsByEmail(email);
    }


    public AuthenticationResponse registerClient(RegisterRequest request) {
        var client = Client.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(validPhoneNumber(request.getPhoneNumber()))
                .username(validUsername(request.getUsername()))
                .build();
        ClientsStatus status = ClientsStatus.builder()
                .client(client)
                .build();
        clientsStatusRepository.save(status);
        clientRepository.save(client);

        var jwtToken = jwtService.generateToken(client);



        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse registerUserAdmin(RegisterRequest request, String role) {
        var userAdmin = UserAdmin.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(validPhoneNumber(request.getPhoneNumber()))
                .username(validUsername(request.getUsername()))
                .role(role)
                .build();
        var settings =
                UserAdminSettingsOfTimetable.builder()
                        .breakInMinutesBetweenOrders(0L)
                        .start(DefaultSettingsOfUserAdminTimetable.START)
                        .finish(DefaultSettingsOfUserAdminTimetable.END)
                        .workDay(true)
                        .userAdmin(userAdmin)
                        .build();
        var status =
                UserAdminStatus.builder()
                        .adminStatus(UserAdminStatuses.VERY_GOOD)
                        .rate(0L)
                        .reports(0L)
                        .build();

        userAdminRepository.save(userAdmin);
        userAdminSettingsOfTimetableRepository.save(settings);
        userAdminStatusRepository.save(status);
        var jwtToken = jwtService.generateToken(userAdmin);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateClient(AuthenticationRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var client = clientRepository.findByEmailOrUsername(request.getEmail(), request.getEmail())
                .orElseThrow(() -> new BadRequestException("Incorrect!"));

        var jwtToken = jwtService.generateToken(client);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateUserAdmin(AuthenticationRequest request) {

        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var userAdmin = userAdminRepository.findByEmailOrUsername(request.getEmail(), request.getEmail())
                .orElseThrow();

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        var jwtToken = jwtService.generateToken(userAdmin);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}