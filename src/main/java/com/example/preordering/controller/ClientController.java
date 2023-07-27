package com.example.preordering.controller;

import com.example.preordering.entity.Client;
import com.example.preordering.entity.ClientsStatus;
import com.example.preordering.entity.UserAdmin;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.model.ClientProfile;
import com.example.preordering.model.UserAdminProfile;
import com.example.preordering.service.JwtService;
import com.example.preordering.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {

    private final UsersService usersService;
    private final JwtService jwtService;

//    @GetMapping("/{username}")
//    public ResponseEntity<?> getUserDetails(@PathVariable String username,
//                                            @NonNull HttpServletRequest request){
//
//
//        Object user = usersService.getByUsername(username);
//
//        if((user instanceof UserAdmin) && ((UserAdmin) user).getUsername().equals(jwtService.getUsernameFromToken(request))) {
//            return ResponseEntity.ok(new UserAdminProfile(
//                    ((UserAdmin) user).getUsername()
//            ));
//        }
//        if (user instanceof Client &&
//                    ((Client) user).getUsername().equals(jwtService.getUsernameFromToken(request))) {
//                user = usersService.getByUsername(username);
//
//                ClientsStatus status =
//                        usersService.getClientStatus(((Client) user).getClientId());
//
//                return ResponseEntity.ok(new ClientProfile(
//                        ((Client) user).getUsername(), ((Client) user).getFirstName(),
//                        ((Client) user).getLastName(), ((Client) user).getEmail(),
//                        ((Client) user).getPhoneNumber(), status.getStatus(), status.getReports()));
//            }
//        throw new BadRequestException("not your page, please login or register to this username!");
//    }
}
