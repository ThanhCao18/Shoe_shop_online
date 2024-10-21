package com.example.ananas.controller;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.response.ApiResponse;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.entity.order.Order;
import com.example.ananas.service.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Tạo đơn hàng
    @PostMapping("/create/{userId}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId") Integer userId, @RequestBody OrderCreate order) {
        return ResponseEntity.ok(orderService.createOrder(userId,order));
    }
    // Xem các đơn hàng mới tạo (PENDING, SHIPPED)
    @GetMapping("/newOrder/{username}")
    public ResponseEntity<List<OrderResponse>> getNewOrder(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getNowOrder(username));
    }
    // Xem lịch sử mua hàng (đã thanh toan)
    @GetMapping("/historyOrder/{username}")
    public ResponseEntity<List<OrderResponse>> getHistoryOrder(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getHistoryOrder(username));
    }
    // Xem các đơn hàng đã hủy
    @GetMapping("/listCancelOrder/{username}")
    public ResponseEntity<List<OrderResponse>> getListCancelOrder(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getCancelOrder(username));
    }
    // Cập nhật đơn hàng
    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable String orderId, @RequestBody OrderUpdateUser orderUpdateUser)
    {
        return ResponseEntity.ok(orderService.updateOrder(orderId,orderUpdateUser));
    }
    // Xem tất cả các đơn hàng theo username
    @GetMapping("/{username}")
    public ResponseEntity<List<OrderResponse>> getOrderByUsername(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getOrderByUsername(username));
    }

    // Hủy đơn hàng
    @GetMapping("/cancelOrder/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        String result = "Failed";
        if(orderService.cancelOrder(orderId)) result = "Successed";
        return ResponseEntity.ok(result);
    }
// Phương thức Admin riêng

    // Xem danh sách đơn hàng
    @GetMapping("/admin/list")
    public ResponseEntity<List<Order>> getOrdersForAdmin () {
        return ResponseEntity.ok(orderService.getOrdersForAdmin());
    }
    //  Cập nhật trạng thái đơn hàng
    @PutMapping("/admin/statusOrderPending/{id}")
    public ResponseEntity<Order> getOrderStatusPending(@PathVariable String id) {
        ApiResponse<Order> apiResponse = new ApiResponse<>();
        return ResponseEntity.ok(orderService.changeOrderStatusPending(id));
    }
    @PutMapping("/admin/statusOrderShipped/{id}")
    public ResponseEntity<Order> getOrderStatusShipped(@PathVariable String id) {
        return ResponseEntity.ok(orderService.changeOrderStatusShipped(id));
    }
    @PutMapping("/admin/statusOrderDelivered/{id}")
    public ResponseEntity<Order> getOrderStatusDelivered(@PathVariable String id) {
        return ResponseEntity.ok(orderService.changeOrderStatusDelivered(id));
    }
    // Cập nhật trạng thái thanh toán
    @PutMapping("/admin/PaymentStatusPaid/{id}")
    public ResponseEntity<Order> changeOrderStatusPaid(@PathVariable String id) {
        return ResponseEntity.ok(orderService.changePaymentStatusPaid(id));
    }
    @PutMapping("/admin/PaymentStatusUnpaid/{id}")
    public ResponseEntity<Order> changeOrderStatusUnpaid(@PathVariable String id) {
        return ResponseEntity.ok(orderService.changePaymentStatusUnPaid(id));
    }
    // Xóa đơn hàng theo ID
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.deleteOrder(id)? "Xóa thành công" : "Xóa không thành công");
    }
}

