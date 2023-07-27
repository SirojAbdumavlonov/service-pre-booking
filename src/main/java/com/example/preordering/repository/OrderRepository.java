package com.example.preordering.repository;

import com.example.preordering.entity.Order;
import com.example.preordering.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT count(o.orderId) FROM Order o WHERE o.userAdmin.userAdminId IN (:ids)"
    )
    Long countTotalOrdersOfCompany(@Param("ids") Set<Long> ids);


}
