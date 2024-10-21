package com.example.ananas.entity;

import com.example.ananas.entity.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "order_item")
public class Order_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "price")
    BigDecimal price;
}

