package com.example.ananas.service.Service;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.request.Order_Items_Create;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.Order_Item_Response;
import com.example.ananas.entity.Order_Item;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.User;
import com.example.ananas.entity.order.Order;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentStatus;
import com.example.ananas.entity.voucher.Voucher;
import com.example.ananas.exception.AppException;
import com.example.ananas.exception.ErrException;
import com.example.ananas.mapper.IOrderMapper;
import com.example.ananas.repository.Order_Repository;
import com.example.ananas.repository.Product_Repository;
import com.example.ananas.repository.User_Repository;
import com.example.ananas.repository.Voucher_Repository;
import com.example.ananas.service.IService.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    @Autowired
    Order_Repository orderRepository;
    @Autowired
    User_Repository userRepository;
    @Autowired
    Voucher_Repository voucherRepository;
    @Autowired
    Product_Repository productRepository;
    @Autowired
    VoucherService voucherService;
    @Autowired
    IOrderMapper orderMapper;

    public List<Order> getOrdersForAdmin() {
       return orderRepository.findAll();
    }

    public List<OrderResponse> getOrderByUsername(String username) {
        List<Order> order = orderRepository.findByUser_Username(username);
        if (order == null) throw new AppException(ErrException.ORDER_NOT_EXISTED);
        return orderMapper.listOrderToOrderResponse(order);
    }

    public OrderResponse createOrder(Integer userId, OrderCreate orderCreate) {
        Order order = new Order();
        if(userId != null)
        {
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty()) throw new AppException(ErrException.USER_NOT_EXISTED);
            order.setUser(user.get());
        }
        if(orderCreate.getCode() != null)
        {
            Voucher voucher = voucherRepository.findVoucherByCode(orderCreate.getCode());
            if(voucher == null) throw new AppException(ErrException.VOUCHER_NOT_EXISTED);
            order.setVoucher(voucher);
        }
        // tổng giá trị của đơn hàng trước khi áp dụng voucher
        List<Order_Items_Create> items = orderCreate.getOrderItems();
        BigDecimal sum_before = BigDecimal.valueOf(0);// tổng giá sản phẩm (đã giảm giá sản sp) chưa có voucher cho toàn bộ đơn hàng

        List<Order_Item> orderItems = new ArrayList<>();
        // Duyệt danh sách chuẩn bị tạo để chuyển thành dạng entity lưu database
        for (Order_Items_Create item : items) {
            Product product = productRepository.findById(item.getProductId()).get();
            if(product == null) throw new AppException(ErrException.ORDER_ERROR_FIND_PRODUCT);
            Order_Item orderItem = new Order_Item();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(product.getDiscount())
                            .divide(BigDecimal.valueOf(100))));
            orderItems.add(orderItem);

            // Tính toán luôn thuộc tính suy biến ở bảng order
            sum_before = sum_before.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotalAmount(sum_before);

        // tổng tổng giá trị của đơn hàng sau khi áp dụng voucher
        BigDecimal sum_after;
        if(order.getVoucher() != null) sum_after = voucherService.applyVoucher(order.getVoucher(), sum_before);
        else sum_after = sum_before;
        order.setTotalPrice(sum_after);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setPaymentMethod(orderCreate.getPaymentMethod());
        order.setRecipientName(orderCreate.getRecipientName());
        order.setRecipientPhone(orderCreate.getRecipientPhone());
        order.setRecipientAddress(orderCreate.getRecipientAddress());
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return orderMapper.orderToOrderResponse(order);
    }

    // Cập nhật đơn hàng
    public OrderResponse updateOrder(String orderId, OrderUpdateUser orderUpdateUser)
    {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isEmpty()) throw new AppException(ErrException.ORDER_NOT_EXISTED);
        order.get().setPaymentMethod(orderUpdateUser.getPaymentMethod());
        order.get().setRecipientName(orderUpdateUser.getRecipientName());
        order.get().setRecipientPhone(orderUpdateUser.getRecipientPhone());
        order.get().setRecipientAddress(orderUpdateUser.getRecipientAddress());
        // Xóa dữ liệu cũ
        order.get().getOrderItems().clear();
        //
        List<Order_Items_Create> newDataRequest = orderUpdateUser.getOrderItems();
        List<Order_Item> newDataEntity = new ArrayList<>();
        BigDecimal sum_before = BigDecimal.valueOf(0);
        BigDecimal sum_after;
        for (Order_Items_Create item : newDataRequest) {
            Product product = productRepository.findById(item.getProductId()).get();
            Order_Item orderItem = new Order_Item();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(product.getDiscount())
                            .divide(BigDecimal.valueOf(100))));
            orderItem.setOrder(order.get());
            newDataEntity.add(orderItem);

            // Tính toán giá trị Total
            sum_before.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.get().setTotalAmount(sum_before);
        order.get().setOrderItems(newDataEntity);
        if(orderUpdateUser.getCode() != null)
        {
            Voucher voucher = voucherRepository.findVoucherByCode(orderUpdateUser.getCode());
            if(voucher == null) throw new AppException(ErrException.VOUCHER_NOT_EXISTED);
            order.get().setVoucher(voucher);
        }
        if(order.get().getVoucher() != null) sum_after = voucherService.applyVoucher(order.get().getVoucher(), sum_before);
        else sum_after = sum_before;
        order.get().setTotalPrice(sum_after);
        orderRepository.save(order.get());
        return orderMapper.orderToOrderResponse(order.get());
    }

    // Trong thực tế không nên xóa hẳn đơn hàng
    public boolean deleteOrder(String orderId) {
        // Tìm đơn hàng theo ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));

        // Kiểm tra trạng thái đơn hàng, ví dụ: không cho phép xóa nếu trạng thái là "đã giao" hoặc "đã thanh toán"
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        orderRepository.delete(order);

        return true; // Trả về true nếu xóa thành công
    }

    public Order changeOrderStatusShipped (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public Order changeOrderStatusDelivered (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setStatus(OrderStatus.DELIVERED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public Order changeOrderStatusPending (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setStatus(OrderStatus.PENDING);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public Order changePaymentStatusPaid (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public Order changePaymentStatusUnPaid (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order);
    }

    public boolean cancelOrder (String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        return true;
    }

    public List<OrderResponse> getNowOrder(String username)
    {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.PENDING, OrderStatus.SHIPPED, OrderStatus.DELIVERED);
        List<Order> orders = orderRepository.findByUser_UsernameAndStatusIn(username, orderStatuses);
        return orderMapper.listOrderToOrderResponse(orders);
    }

    public List<OrderResponse> getCancelOrder(String username)
    {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.CANCELED);
        List<Order> orders = orderRepository.findByUser_UsernameAndStatusIn(username, orderStatuses);
        return orderMapper.listOrderToOrderResponse(orders);
    }

    public List<OrderResponse> getHistoryOrder(String username)
    {
        List<Order> orders = orderRepository.findByUser_UsernameAndPaymentStats(username, PaymentStatus.PAID);
        return orderMapper.listOrderToOrderResponse(orders);
    }
}
