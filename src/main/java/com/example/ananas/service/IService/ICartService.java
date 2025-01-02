package com.example.ananas.service.IService;

import com.example.ananas.dto.request.UserUpdateRequest;
import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.dto.response.UserResponse;
import com.example.ananas.entity.Cart;
import com.example.ananas.entity.Cart_Item;

import java.util.List;

public interface ICartService {
    void addProductToCart(int userId, int productId,int size,String color, int quantity);


    List<CartItemResponse> getAllCartItem(int userId );

    void deleteCart(int userId);

    Integer getSumQuantity(int userId);

    Double getSumPrice(int userId);

    void deleteByVariantId(int userId, int variantId);

    Cart updateCart(int userId, Cart cart);

}
