package com.example.preordering.service;


import com.example.preordering.constants.DefaultSettingsOfUserAdminTimetable;
import com.example.preordering.constants.UserAdminStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.ProfileDetails;
import com.example.preordering.payload.AuthenticationRequest;
import com.example.preordering.payload.AuthenticationResponse;
import com.example.preordering.payload.RegisterRequest;
import com.example.preordering.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final ClientsStatusRepository clientsStatusRepository;
    private final CacheManager cacheManager;
    private final UserAdminSettingOfTimetableRepository userAdminSettingOfTimetableRepository;
    private final UserAdminStatusRepository userAdminStatusRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SessionTokenRepository sessionTokenRepository;

    public Boolean ifUsernameExists(String username){
        return clientRepository.existsByUsername(username) || userAdminRepository.existsByUsername(username);
    }

    private String validPhoneNumber(String phoneNumber){
        if(phoneNumber.startsWith("998") && phoneNumber.length() == 12){
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
    public Boolean isUserRegistered(String email, String role){
        if(role.equals("client")) {
            return clientRepository.existsByEmail(email);
        }
        return userAdminRepository.existsByEmail(email);
    }
    public Boolean isUserAdminRegistered(String email){
        return userAdminRepository.existsByEmail(email);
    }

    public AuthenticationResponse registerUser(RegisterRequest request) {
        if(request.getRole().equals("CLIENT")) {
            var client = Client.builder()

                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .build();
            var status = ClientsStatus.builder()
                    .client(client)
                    .build();

            clientsStatusRepository.save(status);
            clientRepository.save(client);

//            Cache clientStatus = cacheManager.getCache("clientStatus");

//            assert clientStatus != null;
//
//            clientStatus.put(status.getClient().getUsername(), status);
//
//            Cache clients = cacheManager.getCache("clients");
//
//            assert clients != null;
//
//            clients.put(client.getUsername(), client);


            return AuthenticationResponse.builder()
                    .token(jwtService.generateToken(client))
                    .build();
        }
        else {
            var settings =
                UserAdminSettingsOfTimetable.builder()
                        .breakInMinutesBetweenOrders(0L)
                        .start(DefaultSettingsOfUserAdminTimetable.START)
                        .finish(DefaultSettingsOfUserAdminTimetable.END)
                        .build();
            var status =
                    UserAdminStatus.builder()
                            .adminStatus(UserAdminStatuses.VERY_GOOD)
                            .rate(0L)
                            .reports(0L)
                            .build();

            var userAdmin = UserAdmin.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .role(request.getRole().toUpperCase())
                    .userAdminSettingsOfTimetable(settings)
                    .userAdminStatus(status)
                    .build();

            userAdminStatusRepository.save(status);
            userAdminSettingOfTimetableRepository.save(settings);
            userAdminRepository.save(userAdmin);

//            Cache userAdmins = cacheManager.getCache("userAdmins");
//
//            assert userAdmins != null;
//
//            userAdmins.put(userAdmin.getUsername(), userAdmin);
//
//            Cache userAdminsTimetable = cacheManager.getCache("userAdminsTimeTable");
//
//            assert userAdminsTimetable != null;
//
//            userAdminsTimetable.put(userAdmin.getUsername(), settings);
//
//            Cache userAdminStatus = cacheManager.getCache("userAdminsStatus");
//
//            assert userAdminStatus != null;
//
//            userAdminStatus.put(userAdmin.getUsername(), userAdminStatus);

            return AuthenticationResponse.builder()
                    .token(jwtService.generateToken(userAdmin))
                    .build();
        }
    }
    public void createPasswordResetTokenForUser(Object user, String token) {
        PasswordResetToken myToken;
        if(user instanceof UserAdmin userAdmin) {
           myToken = new PasswordResetToken(token, userAdmin, null);
            passwordResetTokenRepository.save(myToken);
        }
        else if (user instanceof Client client) {
            myToken = new PasswordResetToken(token, null, client);
            passwordResetTokenRepository.save(myToken);
        }
    }
    public void addCookie(HttpServletResponse response, AuthenticationRequest request){
        String generatedSessionToken = generateSessionToken();
        Cookie cookie = new Cookie("sessionToken", generatedSessionToken);
        cookie.setMaxAge(60 * 60 * 24 * 15);

        SessionToken sessionToken = SessionToken.builder()
                .sessionToken(generatedSessionToken)
                .emailOrUsername(request.getEmail())
                .expirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15))
                .build();
        response.addCookie(cookie);

        sessionTokenRepository.save(sessionToken);

    }
    public String generateSessionToken(){
        return UUID.randomUUID().toString();
    }

    public AuthenticationResponse authenticateUserAdmin(AuthenticationRequest request) {

//        Authentication authenticate = manager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        String jwtToken;
        if(request.getRole().equals("CLIENT")){
            var client = clientRepository.findByEmailOrUsername(request.getEmail(), request.getEmail()).orElseThrow(() ->
                    new BadRequestException("no such client"));

            jwtToken = jwtService.generateToken(client);

        }
        else {

            var userAdmin = userAdminRepository.findByEmailOrUsernameAndRole(request.getEmail(), request.getEmail(), request.getRole().toUpperCase())
                    .orElseThrow(() -> new BadRequestException("no such user"));
            jwtToken = jwtService.generateToken(userAdmin);

        }
//        SecurityContextHolder.getContext().setAuthentication(authenticate);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public ProfileDetails getProfileDetails(String token){
        String username = jwtService.extractUsername(token);

        UserAdmin userAdmin =
                userAdminRepository.findByUsername(username);

        return new ProfileDetails(userAdmin.getRole(), username, userAdmin.getUserAdminImageName(),
                userAdmin.getFirstName()+ " " + userAdmin.getLastName());
    }
    public void changeUserPassword(Object user, String newPassword){
        if(user instanceof UserAdmin userAdmin){
            userAdmin.setPassword(passwordEncoder.encode(newPassword));
        }
        else if(user instanceof Client client){
            client.setPassword(passwordEncoder.encode(newPassword));
        }
    }
    public String getEmailOrUsernameFromCookie(String sessionToken){
        SessionToken sessionToken1 = sessionTokenRepository.getEmailOrUsernamebySessionToken(sessionToken);
        if(sessionToken1 != null){
            if(sessionToken1.getExpirationDate().after(new Date())){
                return sessionToken1.getEmailOrUsername();
            }
        }
        return "";
    }

}