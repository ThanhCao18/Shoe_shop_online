package com.example.ananas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    int cartId;

    int sumQuantity; // tổng số lượng mặt hàng

    Double sumPrice;//tổng giá tiền

    @ManyToOne
    @JsonBackReference("user-cart")
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "cart")
    @JsonManagedReference
    List<Cart_Item> cartItem;

}
