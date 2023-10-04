package com.example.preordering.repository;

import com.example.preordering.entity.Order;
import com.example.preordering.entity.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (?1) AND o.rate < 3"
    )
    Long countLikes(List<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (?1) AND o.rate > 2"
    )
    Long countDislikes(List<Long> ids);

    @Query(
            "SELECT sum(o.rate) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (?1)"
    )
    double getTotal(List<Long> usernames);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.employeeResponseStatus = ?1 AND o.order.userAdmin.userAdminId IN (?2)"
    )
    Long countSuccessfullOrders(int status, List<Long> usernames);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.employeeResponseStatus = 2 " +
                    "AND o.order.userAdmin.username = ?1"
    )
    Long successfulOrdersOfUserAdmin(String username);

    @Query(
            "SELECT sum(o.order.services.price) FROM OrderStatus o WHERE o.employeeResponseStatus = 1 AND" +
                    " o.order.userAdmin.username = ?1 AND o.order.date = ?2"
    )
    Long sumOfAcceptedOrders(String username, LocalDate date);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.employeeResponseStatus = 1 AND" +
                    " o.order.userAdmin.username = ?1 AND o.order.date = ?2"
    )
    Long countOfAcceptedOrders(String username, LocalDate date);

    @Modifying
    @Transactional
    @Query(
            "UPDATE OrderStatus or SET or.employeeResponseStatus = ?1, or.clientResponseStatus = ?1 WHERE or.order.orderId = ?2"
    )
    void setOrderStatusToDeclined(int status,Long orderId);

    OrderStatus getByOrder_OrderId(Long orderId);

    @Query(
            "UPDATE OrderStatus or SET or.employeeResponseStatus = ?1, or.clientResponseStatus = ?1 WHERE" +
                    "((or.order.start > ?3" +
                    " AND or.order.finish < ?4) OR (or.order.start > ?3 AND or.order.start < ?4)" +
                    " OR (or.order.finish > ?3 AND or.order.finish < ?4))" +
                    " AND or.order.date = ?2 AND or.order.userAdmin.username = ?5"
    )
    void updateToBusy(int orderStatus, LocalDate date, LocalTime start, LocalTime finish, String masterUsername);
}
