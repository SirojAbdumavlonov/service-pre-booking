package com.example.preordering.repository;

import com.example.preordering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT count(o.orderId) FROM Order o WHERE o.userAdmin.userAdminId IN (:ids)"
    )
    Long countTotalOrdersOfCompany(@Param("ids") Set<Long> ids);

    @Query(
            "SELECT c FROM Order c WHERE c.date =: date" +
                    " AND OrderStatus.order.orderId = c.orderId" +
                    " AND OrderStatus.orderStatus = :status " +
                    " AND c.userAdmin.username =: username " +
                    "ORDER BY c.createdAt ASC"
    )
    List<Order> getAllOrders(@Param("date")LocalDate localDate,
                             @Param("status") int status,
                             @Param("username") String username);

    Order getOrderByOrderIdAndClientUsername(Long orderId, String username);



}
