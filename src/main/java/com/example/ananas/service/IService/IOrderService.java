package com.example.ananas.service.IService;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.TempOrder;
import com.example.ananas.entity.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {

    ResultPaginationDTO getOrdersForAdmin(Pageable pageable);

    ResultPaginationDTO getOrderByUsername(String username, Pageable pageable);

    OrderResponse createOrder(Integer userId, OrderCreate orderCreate);

    OrderResponse updateOrder(Integer orderId, OrderUpdateUser orderUpdateUser);

    boolean deleteOrder(Integer orderId);

    OrderResponse changeOrderStatus(Integer orderId, String orderStatus);

    OrderResponse changePaymentStatus(Integer orderId, String paymentStatus);

    boolean cancelOrder(Integer orderId);

    ResultPaginationDTO getOrderByUserNameAndStatusOrder(String username, String status, Pageable pageable);

    ResultPaginationDTO getOrderByStatusOrder(String status, Pageable pageable);

    ResultPaginationDTO  getOrderByUserNameAndPaymentStatus(String username, String paymentStatus, Pageable pageable);

    ResultPaginationDTO getOrderByPaymentStatus(String payMentStatus, Pageable pageable);

    ResultPaginationDTO getOrderByUserId(Integer userId, Pageable pageable);

    OrderResponse getOrderByOrderId(Integer orderId);

    BigDecimal getSumByDay(String date);

    BigDecimal getSumByMonth(int month, int year);

    BigDecimal getSumByYear(int year);

    void handleAfterCreateOrder(int orderId);

    ResultPaginationDTO getAllTempOrder(Specification<TempOrder> spec, Pageable pageable);

    List<TempOrder> getAllTemp();

    Order findOrderByOrderId(Integer orderId);
}
