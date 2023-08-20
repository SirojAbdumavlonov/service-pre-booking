package com.example.preordering.service;

import com.example.preordering.constants.OrderStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.model.OrderTimeService;
import com.example.preordering.model.OrderView;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;
    private final ClientsStatusRepository clientsStatusRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final UserAdminTimeTableRepository userAdminTimeTableRepository;
    private final CompanyRepository companyRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final UserAdminStatusRepository userAdminStatusRepository;
    private final OrderService orderService;

    public Object getByUsername(String username){
        if(clientRepository.findByUsername(username) == null){
            if(userAdminRepository.findByUsername(username) == null){
                throw new UsernameNotFoundException("no user");
            }
            return userAdminRepository.findByUsername(username);
        }
        return clientRepository.findByUsername(username);
    }
    public Long totalSuccessfulOrdersOfUserAdmin(String username){
        return orderStatusRepository.successfulOrdersOfUserAdmin(username);
    }
    public Long countOfSuccessfulOrders(String username, LocalDate localDate){
        return orderStatusRepository.countOfAcceptedOrders(username, localDate);
    }
    public Long totalSumOfSuccessfulOrders(String username, LocalDate localDate){
        return orderStatusRepository.sumOfAcceptedOrders(username, localDate);
    }
    public List<OrderTimeService> getBookedTimes(LocalDate localDate, String username){
        List<LocalTime> starts =
                userAdminTimeTableRepository.getStartsByDate(localDate, username);
        List<LocalTime> ends =
                userAdminTimeTableRepository.getEndsByDate(localDate, username);
        List<String> serviceNames =
                userAdminTimeTableRepository.getServiceNames(localDate, username);
        List<OrderTimeService> booked = new ArrayList<>();
        if(!starts.isEmpty()) {
            for (int i = 0; i < starts.size(); i++) {
                booked.add(
                        new OrderTimeService(starts.get(i),
                                ends.get(i), serviceNames.get(i))
                );
            }
            return booked;
        }
        return null;
    }
//    @Cacheable(value = "companies",key = "#username")
    public String getCompanyName(String username){
        return companyRepository.getCompanyName(username);
    }
    private Company getCompanyByItsNameAndDirectorUsername(String companyName,
                                                           String username){
        return companyRepository.getByCompanyNameAndCompanyDirectorUsername(
                companyName, username);
    }
    public List<String> getOccupationNames(String companyName, String username){
        Company company =
                getCompanyByItsNameAndDirectorUsername(companyName, username);
        if(company == null){
            return new ArrayList<>(Collections.singleton("no occupations"));
        }
        return serviceRepository.occupationNames(company.getCompanyId(),username);
    }

    public List<OrderView> getAllOrders(LocalDate date, String username, String status){
        List<Order> orders;
        if(status.equals("all")){
            orders = orderRepository.getAllOrdersByDate(date, username);
        }
        else {
            orders =
                    orderRepository.getAllOrders(date, getOrderStatus(status), username);
        }
        List<OrderView> orderViews = new ArrayList<>();
        for(Order or: orders){

            orderViews.add(
                    new OrderView(or.getStart(), or.getFinish(),
                            or.getClient().getUsername(), status, or.getServices().getOccupationName(),
                            or.getServices().getCompany().getCompanyName(), or.getOrderId(), or.getCreatedTime())
            );
        }
        return orderViews;
    }
    private static int getOrderStatus(String status){
        return switch (status){
            case "REQUESTED" -> 0;
            case "ACCEPTED" -> 1;
            case "DECLINED" -> -1;
            case "POSTPONED" -> -2;
            case "FINISHED" -> 2;
            case "BUSY" -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
    private static String getStatusString(int num){
        return switch(num){
            case 2 -> "VERY GOOD";
            case 1 -> "GOOD";
            case 0 -> "NORMAL";
            case -1 -> "BAD";
            case -2 -> "VERY BAD";
            default -> throw new IllegalStateException("Unexpected status: " + num);
        };
    }

    public String getStatus(String username){
        return getStatusString(
                userAdminStatusRepository.getUserAdminStatusBy(username));
    }
    public Order getOrderByClientUsernameAndOrderId(String username, Long orderId){
        return orderRepository.getOrderByOrderIdAndClientUsername(orderId, username);
    }

    public void changeStatusToDeclined(Long orderId) {
        orderStatusRepository.setOrderStatusToDeclined(OrderStatuses.DECLINED, orderId);
    }
    public void changeStatusToAccepted(Order order){
        var timeOfTimetable =
                UserAdminTimetable.builder()
                        .start(order.getStart())
                        .finish(order.getFinish())
                        .userAdmin(order.getUserAdmins())
                        .client(order.getClient())
                        .order(order)
                        .date(order.getDate())
                        .build();
        userAdminTimeTableRepository.save(timeOfTimetable);
        OrderStatus orderStatus =
                orderService.getOrderStatusByOrderId(order.getOrderId());
        orderRepository.delete(order);
        orderStatusRepository.delete(orderStatus);
    }
}
