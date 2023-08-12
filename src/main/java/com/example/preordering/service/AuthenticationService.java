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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final CacheManager cacheManager;

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

    public void registerUser(RegisterRequest request) {
        if(request.getRole().equals("client")) {
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

            Cache clientStatus = cacheManager.getCache("clientStatus");

            assert clientStatus != null;

            clientStatus.put(status.getClient().getUsername(), status);

            Cache clients = cacheManager.getCache("clients");

            assert clients != null;

            clients.put(client.getUsername(), client);

            jwtService.generateToken(client);
        }
        else {
            var userAdmin = UserAdmin.builder()

                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .username(request.getUsername())
                    .role(request.getRole().toUpperCase())
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

            Cache userAdmins = cacheManager.getCache("userAdmins");

            assert userAdmins != null;

            userAdmins.put(userAdmin.getUsername(), userAdmin);

            Cache userAdminsTimetable = cacheManager.getCache("userAdminsTimeTable");

            assert userAdminsTimetable != null;

            userAdminsTimetable.put(userAdmin.getUsername(), settings);

            Cache userAdminStatus = cacheManager.getCache("userAdminsStatus");

            assert userAdminStatus != null;

            userAdminStatus.put(userAdmin.getUsername(), userAdminStatus);

            jwtService.generateToken(userAdmin);
        }
    }


    public void authenticateUserAdmin(AuthenticationRequest request) {

        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String jwtToken;
        if(request.getRole().equals("client")){
            var client = clientRepository.findByEmailOrUsername(request.getEmail(), request.getEmail()).orElseThrow(() ->
                    new BadRequestException("no such client"));
            jwtToken = jwtService.generateToken(client);

        }
        else {

            var userAdmin = userAdminRepository.findByEmailOrUsernameAndRole(request.getEmail(), request.getEmail(), request.getRole())
                    .orElseThrow(() -> new BadRequestException("no such user"));
            jwtToken = jwtService.generateToken(userAdmin);

        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}