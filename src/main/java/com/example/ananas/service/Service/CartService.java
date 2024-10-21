package com.example.ananas.service.Service;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.Cart;
import com.example.ananas.entity.Cart_Item;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.User;
import com.example.ananas.mapper.ICartItemMapper;
import com.example.ananas.repository.Cart_Item_Repository;
import com.example.ananas.repository.Cart_Repository;
import com.example.ananas.repository.Product_Repository;
import com.example.ananas.repository.User_Repository;
import com.example.ananas.service.IService.ICartService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService implements ICartService {
    Cart_Repository cartRepository;
    User_Repository userRepository;
    Product_Repository productRepository;
    Cart_Item_Repository cartItemRepository;
    ICartItemMapper cartItemMapper;
    @Override
    public void addProductToCart(int userId, int productId, int quantity) {
        User user = this.userRepository.findById(userId).get();
        if (this.userRepository.existsById(userId)) {
            //kiem tra user da ton tai cart nao chua neu chua co thi tao moi
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                //tao moi cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSumQuantity(0);
                otherCart.setSumPrice(0.0);
                cart = this.cartRepository.save(otherCart);
            }

            //kiem tra san pham co ton tai khong
            Optional<Product> productOptional = this.productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();

                //kiem tra xem trong gio hang da tung co san pham nay chua, neu co thi tang so luong, neu chua thi them sp vao gio hang
                Cart_Item oldCartItem = this.cartItemRepository.findByCartAndProduct(cart, realProduct);
                if (oldCartItem == null) //sp chua ton tai trong gio hang
                {
                    Cart_Item cart_item = new Cart_Item();
                    cart_item.setCart(cart);
                    cart_item.setProduct(realProduct);
                    cart_item.setQuantity(quantity);
                    this.cartItemRepository.save(cart_item);

                    //
                    int s = cart.getSumQuantity() +1;
                    cart.setSumQuantity(s);
                    this.cartRepository.save(cart);
                } else {
                    oldCartItem.setQuantity(oldCartItem.getQuantity() + quantity);
                    this.cartItemRepository.save(oldCartItem);
                }
            }
        }
    }

    @Override
    public List<CartItemResponse> getAllCartItem(int userId) {
        Cart cart = this.cartRepository.findByUser(this.userRepository.findById(userId).get());
        return this.cartItemMapper.toCartItemResponseList(this.cartItemRepository.findCart_ItemsByCart(cart));
    }

    @Override
    @Transactional
    public void deleteCart(int userId) {
        User user = this.userRepository.findById(userId).get();
        Cart cartDelete = this.cartRepository.findByUser(user);
        this.cartItemRepository.deleteByCart(cartDelete);
        this.cartRepository.deleteByUser(user);

    }

    @Override
    public Integer getSumQuantity(int userId) {
        User user = this.userRepository.findById(userId).get();
        Cart currentCart = this.cartRepository.findByUser(user);
        return currentCart.getSumQuantity();
    }
}
