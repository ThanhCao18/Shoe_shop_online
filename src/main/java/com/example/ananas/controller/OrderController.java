package com.example.ananas.controller;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.service.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/list")
    public ResponseEntity<ResultPaginationDTO> getOrdersForAdmin (Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersForAdmin(pageable));
    }

    // Xem tất cả các đơn hàng theo username
    @GetMapping("/{username}")
    public ResponseEntity<ResultPaginationDTO> getOrderByUsername(@PathVariable String username, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderByUsername(username, pageable));
    }

    // Tạo đơn hàng
    @PostMapping("/create/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId") Integer userId, @RequestBody OrderCreate order) {
        return ResponseEntity.ok(orderService.createOrder(userId,order));
    }

    // Cập nhật đơn hàng
    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Integer orderId, @RequestBody OrderUpdateUser orderUpdateUser)
    {
        return ResponseEntity.ok(orderService.updateOrder(orderId,orderUpdateUser));
    }

    // Xóa đơn hàng theo ID
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.deleteOrder(id)? "Xóa thành công" : "Xóa không thành công");
    }

    // Hủy đơn hàng
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        String result = "Failed";
        if(orderService.cancelOrder(orderId)) result = "Successed";
        return ResponseEntity.ok(result);
    }

    //  Cập nhật trạng thái đơn hàng
    @PutMapping("/admin/statusOrder/{id}")
    public ResponseEntity<OrderResponse> getOrderStatus(@PathVariable Integer id, @RequestParam("status") String status) {
        return ResponseEntity.ok(orderService.changeOrderStatus(id, status));
    }
    @PutMapping("/admin/paymentStatus/{id}")
    public ResponseEntity<OrderResponse> getOrderPaymentStatus(@PathVariable Integer id, @RequestParam("paymentStatus") String paymentStatus) {
        return ResponseEntity.ok(orderService.changePaymentStatus(id, paymentStatus));
    }

    @GetMapping("/listOrderByStatus/{username}") //
    public ResponseEntity<ResultPaginationDTO> getOrdersByStatus(@PathVariable String username, @RequestParam("status") String status, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderByUserNameAndStatusOrder(username, status, pageable));
    }
    @GetMapping("/admin/listOrderByStatus")
    public ResponseEntity<ResultPaginationDTO> getOrdersByStatusForAdmin(@RequestParam("status") String status, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderByStatusOrder(status, pageable));
    }

    @GetMapping("/listOrderByPaymentStatus/{username}")
    public ResponseEntity<ResultPaginationDTO> getOrderByUserNameAndPaymentStatus(@PathVariable String username, @RequestParam("paymentStatus") String paymentStatus, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderByUserNameAndPaymentStatus(username, paymentStatus, pageable));
    }
    @GetMapping("/admin/listOrderByPaymentStatus")
    public ResponseEntity<ResultPaginationDTO> getOrderByPaymentStatus(@RequestParam("paymentStatus") String paymentStatus, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderByPaymentStatus(paymentStatus, pageable));
    }

}

