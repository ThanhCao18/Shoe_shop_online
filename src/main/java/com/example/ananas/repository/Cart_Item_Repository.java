package com.example.ananas.repository;

import com.example.ananas.entity.Cart;
import com.example.ananas.entity.Cart_Item;
import com.example.ananas.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cart_Item_Repository extends JpaRepository<Cart_Item, Integer> {
    Cart_Item findByCartAndProductVariant(Cart cart, ProductVariant productVariant);
    List<Cart_Item> findCart_ItemsByCart(Cart cart);

    void deleteByCart(Cart cartDelete);
    void deleteByProductVariant(ProductVariant productVariant);
    void deleteByCartAndProductVariant(Cart cart, ProductVariant productVariant);
}
