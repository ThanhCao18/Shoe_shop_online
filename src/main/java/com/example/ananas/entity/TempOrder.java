package com.example.ananas.entity;

import com.example.ananas.dto.response.Order_Item_Response;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentMethod;
import com.example.ananas.entity.order.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "temp-order")
public class TempOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String txnRef;

    int userId;

    String code;

    BigDecimal totalAmount;

    BigDecimal totalPrice;

    OrderStatus status;

    PaymentMethod paymentMethod;

    PaymentStatus paymentStatus;

    String recipientName;

    String recipientPhone;

    String recipientAddress;

//    List<Order_Item_Response> orderItems;
    private Instant createdAt;

    private Instant updateAt;


    @PrePersist
    public void handleBeforeCreate()
    {
        this.createdAt = Instant.now();
    }
    @PreUpdate
    public void handleBeforeUpdate()
    {
        this.updateAt = Instant.now();
    }
}
