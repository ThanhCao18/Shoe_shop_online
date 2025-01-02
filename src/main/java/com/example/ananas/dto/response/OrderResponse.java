package com.example.ananas.dto.response;

import com.example.ananas.entity.Order_Item;
import com.example.ananas.entity.User;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentMethod;
import com.example.ananas.entity.order.PaymentStatus;
import com.example.ananas.entity.voucher.Voucher;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    Integer id;

    int userId;

    String code;

    BigDecimal totalAmount;

    BigDecimal discount_voucher;

    BigDecimal totalPrice;

    OrderStatus status;

    PaymentMethod paymentMethod;

    PaymentStatus paymentStatus;

    String recipientName;

    String recipientPhone;

    String recipientAddress;

    String description;

    Timestamp createdAt;

    Timestamp updatedAt;

    List<Order_Item_Response> orderItems;
}
