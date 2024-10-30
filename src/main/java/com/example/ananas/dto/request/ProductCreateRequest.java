package com.example.ananas.dto.request;

import com.example.ananas.dto.ProductVatriantDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    String productName;
    String description;
    double price;
    double discount;
    String material;
    String category;
    List<ProductVatriantDTO> variants; // danh sach ca bien the
}
