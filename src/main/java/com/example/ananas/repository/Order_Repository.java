package com.example.ananas.repository;

import com.example.ananas.entity.order.Order;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Order_Repository extends JpaRepository<Order, String> {

    List<Order> findByUser_Username(String username);

    List<Order> findByUser_UsernameAndStatusIn(String username, List<OrderStatus> statuses);

    /*
        @Query("SELECT o FROM Order o WHERE o.user.username = :username AND o.status IN (:statuses)")
    List<Order> findOrdersByUsernameAndStatuses(@Param("username") String username, @Param("statuses") List<OrderStatus> statuses);
    */

    /*
        @Query(value = "SELECT * FROM orders o WHERE o.user_id = (SELECT u.user_id FROM users u WHERE u.username = :username) AND o.status IN (:statuses)", nativeQuery = true)
    List<Order> findOrdersByUsernameAndStatusesNative(@Param("username") String username, @Param("statuses") List<String> statuses);
    */

    @Query(value = "SELECT o FROM Order o WHERE o.user.username = :username AND o.paymentStatus = :status")
    List<Order> findByUser_UsernameAndPaymentStats(@Param("username") String username, @Param("status") PaymentStatus paymentStatus);
}
