package com.example.ananas.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    int productId;
    String productName;
    String description;
    double price;
    int stock;
    double discount;
    int size;
    String color;
    String material;
    String category;
}
