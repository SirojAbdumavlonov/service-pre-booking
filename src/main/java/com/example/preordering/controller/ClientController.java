package com.example.preordering.controller;

import com.example.preordering.entity.Client;
import com.example.preordering.model.ClientProfile;
import com.example.preordering.model.OrderView;
import com.example.preordering.service.ClientService;
import com.example.preordering.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController{

    private final UserAdminService userAdminService;
    private final ClientService clientService;

}
