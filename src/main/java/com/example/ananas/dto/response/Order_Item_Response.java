package com.example.ananas.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order_Item_Response {
    int productId;
    String productName;
    Integer quantity;
    String description;
    double price_original;
    double discount;
    String image;
    BigDecimal price;
}
