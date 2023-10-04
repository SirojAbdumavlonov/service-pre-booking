package com.example.preordering.service;

import com.example.preordering.entity.UserAdmin;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final UserAdminRepository userAdminRepository;
    public Long getClientReports(String username){
        UserAdmin userAdmin =
                userAdminRepository.findByUsername(username);

        return userAdmin.getUserAdminStatus().getReports();
    }
    public String getClientStatus(String username){
        UserAdmin userAdmin =
                userAdminRepository.findByUsername(username);

        return getStatus(userAdmin.getUserAdminStatus().getAdminStatus());
    }
    public String getStatus(int levelOfStatus){
        return switch(levelOfStatus){
            case 1 -> "LOYAL";
            case 2 -> "ACTIVE";
            case -1 -> "NOT LOYAL";
            case -2 -> "NONACTIVE";
            default -> throw new IllegalStateException("Unexpected value: " + levelOfStatus);
        };
    }


}
