package com.example.ananas.service.IService;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.Cart_Item;

import java.util.List;

public interface ICartService {
    void addProductToCart(int userId, int productId, int quantity);
    List<CartItemResponse> getAllCartItem(int userId );

    void deleteCart(int userId);

    Integer getSumQuantity(int userId);
}
