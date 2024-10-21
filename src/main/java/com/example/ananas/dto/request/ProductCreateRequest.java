package com.example.ananas.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
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
