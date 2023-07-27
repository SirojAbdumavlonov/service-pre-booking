package com.example.preordering.service;


import com.example.preordering.entity.*;
import com.example.preordering.payload.OrderRequest;
import com.example.preordering.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;

    @Transactional
    public Order bookAnOrder(Service service,
                             OrderRequest orderRequest,
                             HttpServletRequest request){

        String username =
                jwtService.getUsernameFromToken(request);
        Client client =
                clientRepository.findByUsername(username);
        UserAdmin master =
                userAdminRepository.findByUsername(orderRequest.getUsername());

        var order = Order.builder()
                .services(service)
                .userAdmin(master)
                .start(LocalTime.parse(orderRequest.getStart()))
                .date(LocalDate.parse(orderRequest.getDate()))
                .finish(LocalTime.parse(orderRequest.getFinish()))
                .client(client)
                .build();

        var orderStatus =
                OrderStatus.builder()
                        .order(order)
                        .build();

        var userAdminTimeTable = UserAdminTimetable.builder()
                .client(client)
                .userAdmin(master)
                .start(LocalTime.parse(orderRequest.getStart()))
                .finish(LocalTime.parse(orderRequest.getFinish()))
                .date(LocalDate.parse(orderRequest.getDate()))
                .order(order)
                .build();

        userAdminTimeTableRepository.save(userAdminTimeTable);
        orderRepository.save(order);
        orderStatusRepository.save(orderStatus);
        return order;
    }


}
