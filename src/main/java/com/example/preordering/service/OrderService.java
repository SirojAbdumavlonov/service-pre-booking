package com.example.preordering.service;


import com.example.preordering.constants.GeneralStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.payload.OrderRequest;
import com.example.preordering.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserAdminRepository userAdminRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;

    @Transactional
    public Order bookAnOrder(Service service,
                             OrderRequest orderRequest,
                             HttpServletRequest request){

        String username =
                jwtService.getUsernameFromToken(request);
        System.out.println("username = " + username);
        UserAdmin client =
                userAdminRepository.findClientByUsername(username);
        System.out.println("client = " + client);
        UserAdmin master =
                userAdminRepository.findByUsername(orderRequest.getUsername());
        System.out.println("master = " + master);

        var order = Order.builder()
                .services(service)
                .userAdmin(master)
                .start(LocalTime.parse(orderRequest.getStart()))
                .date(LocalDate.parse(orderRequest.getDate()))
                .finish(LocalTime.parse(orderRequest.getFinish()))
                .client(client)
                .status(GeneralStatuses.ACTIVE)
                .build();

        var orderStatus =
                OrderStatus.builder()
                        .order(order)
                        .build();


        orderRepository.save(order);
        orderStatusRepository.save(orderStatus);
        return order;
    }
    public OrderStatus getOrderStatusByOrderId(Long orderId){
            return orderStatusRepository.getByOrder_OrderId(orderId);

    }


}
