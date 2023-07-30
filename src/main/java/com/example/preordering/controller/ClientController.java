package com.example.preordering.controller;

import com.example.preordering.service.JwtService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClientController {

    private final UserAdminService userAdminService;
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
