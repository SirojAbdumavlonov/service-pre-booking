package com.example.preordering.service;

import com.example.preordering.entity.ClientsStatus;
import com.example.preordering.exception.BadRequestException;
import com.example.preordering.repository.ClientRepository;
import com.example.preordering.repository.ClientsStatusRepository;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final ClientsStatusRepository clientsStatusRepository;

    public Object getByUsername(String username){
        if(clientRepository.findByUsername(username) == null){
            if(userAdminRepository.findByUsername(username) == null){
                throw new UsernameNotFoundException("no user");
            }
            return userAdminRepository.findByUsername(username);
        }
        return clientRepository.findByUsername(username);
    }
    public ClientsStatus getClientStatus(Long id){
        return clientsStatusRepository.findByClient_ClientId(id)
                .orElseThrow(() -> new BadRequestException("incorrect data of client"));
    }
}
