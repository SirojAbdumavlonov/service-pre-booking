package com.example.preordering.service;

import com.example.preordering.constants.OrderStatuses;
import com.example.preordering.entity.*;
import com.example.preordering.model.OrderTimeService;
import com.example.preordering.model.OrderView;
import com.example.preordering.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        return serviceRepository.occupationNames(company.getCompanyId(),username);
    }

    public List<OrderView> getAllOrders(LocalDate date, String username, String status){
        List<Order> orders =
                orderRepository.getAllOrders(date, OrderStatuses.WAITING,username);
        List<OrderView> orderViews = new ArrayList<>();
        for(Order or: orders){

            orderViews.add(
                    new OrderView(or.getStart(), or.getFinish(),
                            or.getClient().getUsername(), status, or.getServices().getOccupationName(),
                            or.getServices().getCompany().getCompanyName(), or.getOrderId())
            );
        }
        return orderViews;
    }
    private static String getStatusString(int num){
        List<String> statuses = new ArrayList<>();
        statuses.add("very bad");
        statuses.add("bad");
        statuses.add("norm");
        statuses.add("good");
        statuses.add("very good");
        return statuses.get(num+2);
    }
    public UserAdmin getUserAdminByUsername(String username){
        HashMap<String, UserAdmin> userAdmins =
                userAdminRepository.allUserAdmins();
        return userAdmins.get(username);
    }
    public Client getClientByUsername(String username){
        HashMap<String, Client> clients =
                clientRepository.allClients();
        return clients.get(username);
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
                        .userAdmin(getUserAdminByUsername(order.getUserAdmins().getUsername()))
                        .client(getClientByUsername(order.getClient().getUsername()))
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
