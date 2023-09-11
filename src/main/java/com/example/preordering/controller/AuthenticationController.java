package com.example.preordering.controller;

import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.ProfileDetails;
import com.example.preordering.model.SavedPassword;
import com.example.preordering.payload.AuthenticationRequest;
import com.example.preordering.payload.AuthenticationResponse;
import com.example.preordering.payload.RegisterRequest;
import com.example.preordering.service.AuthenticationService;
import com.example.preordering.service.UserAdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserAdminService userAdminService;
    private JavaMailSender mailSender;

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

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse response
    ){
        AuthenticationResponse authenticationResponse =
                service.authenticateUserAdmin(request);
        service.addCookie(response, request);
//        return ResponseEntity.ok(new ApiResponse("You are logged in successfully!"));
        return ResponseEntity.ok(authenticationResponse);

    }
    @PostMapping("/user_profile")
    public ResponseEntity<?> getProfileDetails(@RequestParam(name = "token") String token){
        ProfileDetails profileDetails = service.getProfileDetails(token);

        return ResponseEntity.ok(profileDetails);
    }

    @GetMapping("/signin")
    public ResponseEntity<?> signinPage(@CookieValue("sessionToken") String token){
        String sessionToken = "";
        if(token != null){
            sessionToken = service.getEmailOrUsernameFromCookie(token);
        }
        return ResponseEntity.ok(sessionToken);
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request,
                                  @RequestParam("email") String userEmail) {
        Object user = userAdminService.getByUsername(userEmail);
        if (user == null) {
            throw new BadRequestException("User not found");
        }
        String token = UUID.randomUUID().toString();
        service.createPasswordResetTokenForUser(user, token);
        mailSender.send(userAdminService.constructResetTokenEmail(UserAdminService.getSiteURL(request),
                request.getLocale(), token, userAdminService.getEmailOfUser(user)));
        return ResponseEntity.ok("Sent!");
    }
    @GetMapping("/user/changePassword")
    public ResponseEntity<?> showChangePasswordPage(@RequestParam("token") String token) {
        String result = userAdminService.validatePasswordResetToken(token);
        if(result != null){
            return ResponseEntity.ok("Verified");
        }
        else {
            return ResponseEntity.ok("Not verified!");
        }

//        if(result != null) {
//            String message = messages.getMessage("auth.message." + result, null, locale);
//            return "redirect:/login.html?lang="
//                    + locale.getLanguage() + "&message=" + message;
//        } else {
//            model.addAttribute("token", token);
//            return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
//        }
    }
    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(final Locale locale, SavedPassword savedPassword) {

        String result = userAdminService.validatePasswordResetToken(savedPassword.getToken());

        if(result != null) {
            return ResponseEntity.ok(result);
//            return new GenericResponse(messages.getMessage(
//                    "auth.message." + result, null, locale));
        }

        Optional<Object> user = userAdminService.getUserByPasswordResetToken(savedPassword.getToken());
        if(user.isPresent()) {
            service.changeUserPassword(user.get(), savedPassword.getPassword());
//            return new GenericResponse(messages.getMessage(
//                    "message.resetPasswordSuc", null, locale));
            return ResponseEntity.ok("Successful change!");
        } else {
//            return new GenericResponse(messages.getMessage(
//                    "auth.message.invalid", null, locale));
            return ResponseEntity.ok("Invalid");
        }
    }



}