package com.example.preordering.repository;

import com.example.preordering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT count(o.orderId) FROM Order o WHERE o.userAdmin.userAdminId IN (?1)"
    )
    Long countTotalOrdersOfCompany(List<Long> ids);

    @Query(
            "SELECT c FROM Order c WHERE c.date = ?1" +
                    " AND c.orderStatus.order.orderId = c.orderId" +
                    " AND c.userAdmin.username = ?2" +
                    " AND c.status = 'ACTIVE'" +
                    " ORDER BY c.createdTime ASC"
    )
    List<Order> getAllOrdersByDate(LocalDate localDate, String username);

    @Query(
            "SELECT c FROM Order c WHERE c.date = ?1" +
                    " AND c.orderStatus.order.orderId= c.orderId" +
                    " AND c.orderStatus.orderStatus = ?2" +
                    " AND c.userAdmin.username = ?3" +
                    " AND c.status = 'ACTIVE'" +
                    " ORDER BY c.createdTime ASC"
    )
    List<Order> getAllOrders(LocalDate localDate,
                             int status,
                             String username);

    Order getOrderByOrderIdAndClientUsernameAndStatus(Long orderId, String username, String status);



}
