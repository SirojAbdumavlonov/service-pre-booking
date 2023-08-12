package com.example.preordering.service;

import com.example.preordering.repository.ClientsStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientsStatusRepository clientsStatusRepository;

    public int getClientReports(String username){
        return clientsStatusRepository.getReports(username);
    }
    public String getClientStatus(String username){
        return getStatus(clientsStatusRepository.getStatus(username));
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
