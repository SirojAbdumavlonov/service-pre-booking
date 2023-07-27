package com.example.preordering.repository;

import com.example.preordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (:ids) AND o.rate = 1"
    )
    Long countLikes(@Param("ids") Set<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.order.userAdmin.userAdminId IN (:ids) AND o.rate = -1"
    )
    Long countDislikes(@Param("ids") Set<Long> ids);

    @Query(
            "SELECT count(o) FROM OrderStatus o WHERE o.orderStatus = :status AND o.order.userAdmin IN (:ids)"
    )
    Long countSuccessfullOrders(@Param("status") String status, @Param("ids") Set<Long> ids);
}
