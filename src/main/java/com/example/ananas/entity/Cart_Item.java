package com.example.ananas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart_item")
public class Cart_Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    int cartItemId;



    @Column(name = "quantity")
    int quantity;



    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cart_id")
    Cart cart;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    Product product;

}
