package com.example.ananas.controller;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.Cart_Item;
import com.example.ananas.repository.User_Repository;
import com.example.ananas.service.Service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class CartController {
    CartService cartService;
    User_Repository userRepository;

    // thêm một sản phẩm vào giỏ hàng
    @PostMapping("/cart")
    public ResponseEntity<String> addProductToCart(@RequestParam(name = "userId") int userId,
                                                   @RequestParam(name = "productId") int productId, @RequestParam(name = "quantity") int quantity,
                                                   @RequestParam(name = "size") int size, @RequestParam(name = "color") String color)
    {

        if(!this.userRepository.existsById(userId))
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi thêm vào giỏ hàng: id người dùng không tồn tại" );
        }
        try {
            this.cartService.addProductToCart(userId, productId,size, color, quantity);
            return ResponseEntity.ok("Thêm thành công vào giỏ hàng");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }
    @GetMapping("/cart")
    public ResponseEntity<List<CartItemResponse>> getAllCartItem(@RequestParam int userId)
    {
        return ResponseEntity.ok(this.cartService.getAllCartItem(userId));
    }
    @DeleteMapping("/cart")
    public ResponseEntity<String> deleteCart(@RequestParam(name = "userId") int userId)
    {
        try {
            this.cartService.deleteCart( userId);
            return ResponseEntity.ok("xoa thành công giỏ hàng của người dùng có id "+ userId);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("xay ra loi: "+ e.getMessage());
        }
    }
    @GetMapping("/cart/quantity")
    public ResponseEntity<Integer> getSumQuantity(@RequestParam int userId)
    {
        return ResponseEntity.ok(this.cartService.getSumQuantity(userId) );
    }

}
