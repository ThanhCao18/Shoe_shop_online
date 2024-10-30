package com.example.ananas.repository;

import com.example.ananas.entity.order.Order;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Order_Repository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    /*
        Page<Order> findByUser_Username(String username, Pageable pageable);

    Page<Order> findByUser_UsernameAndStatus(String username, OrderStatus status, Pageable pageable);

        @Query("SELECT o FROM Order o WHERE o.user.username = :username AND o.status IN (:statuses)")
    List<Order> findOrdersByUsernameAndStatuses(@Param("username") String username, @Param("statuses") List<OrderStatus> statuses);

        @Query(value = "SELECT * FROM orders o WHERE o.user_id = (SELECT u.user_id FROM users u WHERE u.username = :username) AND o.status IN (:statuses)", nativeQuery = true)
    List<Order> findOrdersByUsernameAndStatusesNative(@Param("username") String username, @Param("statuses") List<String> statuses);

    @Query(value = "SELECT o FROM Order o WHERE o.user.username = :username AND o.paymentStatus = :status")
    List<Order> findByUser_UsernameAndPaymentStats(@Param("username") String username, @Param("status") PaymentStatus paymentStatus);

     */
}
