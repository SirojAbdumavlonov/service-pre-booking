package com.example.preordering.repository;

import com.example.preordering.entity.Order;
import com.example.preordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (:ids) AND o.rate = 1"
    )
    Long countLikes(@Param("ids") List<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (:ids) AND o.rate = -1"
    )
    Long countDislikes(@Param("ids") List<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.orderStatus = :status AND o.order.userAdmin.userAdminId IN (:ids)"
    )
    Long countSuccessfullOrders(@Param("status") int status, @Param("ids") List<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.orderStatus = 2 " +
                    "AND o.order.userAdmin.username =: username"
    )
    Long successfulOrdersOfUserAdmin(@Param("username") String username);

    @Query(
            "SELECT sum(o.order.services.price) FROM OrderStatus o WHERE o.orderStatus = 1 AND" +
                    " o.order.userAdmin.username =: username AND o.order.date = :date"
    )
    Long sumOfAcceptedOrders(@Param("username") String username, @Param("date")LocalDate date);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.orderStatus = 1 AND" +
                    " o.order.userAdmin.username =: username AND o.order.date = :date"
    )
    Long countOfAcceptedOrders(@Param("username") String username, @Param("date")LocalDate date);

    @Modifying
    @Query(
            "UPDATE OrderStatus or SET or.orderStatus =: status WHERE or.order.orderId =: orderId"
    )
    void setOrderStatusToDeclined(@Param("status") int status, @Param("orderId") Long orderId);

    @Query(
            "SELECT CS.order.orderId,CS FROM OrderStatus CS"
    )
    HashMap<Long, OrderStatus> getAllOrderStatuses();
}
