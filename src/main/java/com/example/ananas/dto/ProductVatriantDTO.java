package com.example.ananas.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVatriantDTO {

    int stock;
    int size;
    String color;
    int productId;
}
