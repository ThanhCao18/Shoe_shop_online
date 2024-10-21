package com.example.ananas.mapper;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.Cart_Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartItemMapper {
    @Mapping(target = "product", source = "product.productName")
    @Mapping(target = "productId", source = "product.productId")
    CartItemResponse toCartItemResponse(Cart_Item cartItem);

    List<CartItemResponse> toCartItemResponseList(List<Cart_Item> cartItemList);
}
