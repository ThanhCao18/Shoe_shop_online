package com.example.ananas.service.Service;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.*;
import com.example.ananas.exception.AppException;
import com.example.ananas.exception.ErrException;
import com.example.ananas.mapper.ICartItemMapper;
import com.example.ananas.repository.*;
import com.example.ananas.service.IService.ICartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    ProductVariant_Repository productVariantRepository;
    @Override
    @Transactional
    public void addProductToCart(int userId, int productId,int size, String color, int quantity) {
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
            Product product = this.productRepository.findById(productId).get();
//            ProductVariant productVariantOptional = this.productVariantRepository.findProductVariantByProductAndColorAndSize(product,color,size);
            ProductVariant productVariantOptional = new ProductVariant();
            try {
               productVariantOptional  = this.productVariantRepository.findByProductColorAndSize(productId,color,size);
            }
            catch (Exception e)
            {
                e.getMessage();
            }

            System.out.println("Product ID: " + productId);
            System.out.println("Color: " + color);
            System.out.println("Size: " + size);

//            ProductVariant productVariantOptional = this.testService.findProductVariant(productId,color,size);
            if (productVariantOptional != null) {
                ProductVariant realProduct = productVariantOptional;

                //kiem tra xem trong gio hang da tung co bien the san pham nay chua, neu co thi tang so luong, neu chua thi them sp vao gio hang
                Cart_Item oldCartItem = this.cartItemRepository.findByCartAndProductVariant(cart, realProduct);
                if (oldCartItem == null) //sp chua ton tai trong gio hang
                {
                    Cart_Item cart_item = new Cart_Item();
                    cart_item.setCart(cart);
                    cart_item.setProductVariant(realProduct);
                    cart_item.setQuantity(quantity);
                    this.cartItemRepository.save(cart_item);


                    int s = cart.getSumQuantity() +1;
                    double price = cart.getSumPrice() + this.productRepository.findById(productId).get().getPrice() * quantity;
                    cart.setSumQuantity(s);
                    cart.setSumPrice(price);
                    this.cartRepository.save(cart);
                } else {
                    oldCartItem.setQuantity(oldCartItem.getQuantity() + quantity);
                    double price = cart.getSumPrice() + this.productRepository.findById(productId).get().getPrice() * quantity;
                    cart.setSumPrice(price);
                    this.cartRepository.save(cart);
                    this.cartItemRepository.save(oldCartItem);
                }
            }
            else {
                System.out.println("Product Variant not found.");
            }
        }

    }

    @Override
    public List<CartItemResponse> getAllCartItem(int userId) {
        Cart cart = this.cartRepository.findByUser(this.userRepository.findById(userId).get());
        List<Cart_Item> cartItemList = this.cartItemRepository.findCart_ItemsByCart(cart);
        return this.cartItemMapper.toCartItemResponseList(cartItemList);
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

    @Override
    public Double getSumPrice(int userId) {
        User user = this.userRepository.findById(userId).get();
        Cart currentCart = this.cartRepository.findByUser(user);
        return currentCart.getSumPrice();
    }

    @Transactional
    @Override
    public void deleteByVariantId(int userId, int variantId) {
        User user = this.userRepository.findById(userId).get();
        Cart currentCart = this.cartRepository.findByUser(user);
        ProductVariant productVariant = this.productVariantRepository.findById(variantId).get();
        Product product = productVariant.getProduct();
        this.cartItemRepository.deleteByCartAndProductVariant(currentCart,productVariant);
        currentCart.setSumPrice(currentCart.getSumPrice()-product.getPrice());
        currentCart.setSumQuantity(currentCart.getSumQuantity()-1);

        List<Cart_Item> cartItemList = this.cartItemRepository.findCart_ItemsByCart(currentCart);
        if(cartItemList.size() == 0)
            deleteCart(userId);
    }

    @Override
    public Cart updateCart(int userid, Cart cart) {
        User user = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cartUpdate = cartRepository.findByUser(user);
        cartUpdate.setSumQuantity(cart.getSumQuantity());
        cartUpdate.setSumPrice(cart.getSumPrice());
        return cartRepository.save(cartUpdate);
    }
}
