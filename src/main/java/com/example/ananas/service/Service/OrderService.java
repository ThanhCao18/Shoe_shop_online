package com.example.ananas.service.Service;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.request.Order_Items_Create;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.Order_Item;
import com.example.ananas.entity.ProductVariant;
import com.example.ananas.entity.User;
import com.example.ananas.entity.order.Order;
import com.example.ananas.entity.order.OrderSpecification;
import com.example.ananas.entity.order.OrderStatus;
import com.example.ananas.entity.order.PaymentStatus;
import com.example.ananas.entity.voucher.Voucher;
import com.example.ananas.exception.AppException;
import com.example.ananas.exception.ErrException;
import com.example.ananas.mapper.IOrderMapper;
import com.example.ananas.repository.*;
import com.example.ananas.service.IService.IOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Order_Repository orderRepository;

    User_Repository userRepository;

    Voucher_Repository voucherRepository;

    ProductVariant_Repository productVariantRepository;

    Product_Repository productRepository;

    VoucherService voucherService;

    IOrderMapper orderMapper;

    @Override
    public ResultPaginationDTO getOrdersForAdmin(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        ResultPaginationDTO re  = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        re.setMeta(mt);
        re.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
       return re;
    }

    @Override
    public ResultPaginationDTO getOrderByUsername(String username, Pageable pageable) {
        // Page<Order> orders = orderRepository.findByUser_Username(username, pageable);
        // Page<Order> orders = orderRepository.findByUser_Username(username, pageable);
        OrderSpecification specification =  new OrderSpecification(username, null, null);
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        res.setMeta(mt);
        res.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
        return res;
    }

    @Override
    public OrderResponse createOrder(Integer userId, OrderCreate orderCreate) {
        Order order = new Order();
        if(userId != null)
        {
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty()) order.setUser(null);
            else order.setUser(user.get());
        }else order.setUser(null);

        if(orderCreate.getCode() != null)
        {
            Voucher voucher = voucherRepository.findVoucherByCode(orderCreate.getCode());
            if(voucher == null) throw new AppException(ErrException.VOUCHER_NOT_EXISTED);
            order.setVoucher(voucher);

        } else order.setVoucher(null);

        // tổng giá trị của đơn hàng trước khi áp dụng voucher
        List<Order_Items_Create> items = orderCreate.getOrderItems();
        BigDecimal sum_before = BigDecimal.valueOf(0);// tổng giá sản phẩm (đã giảm giá sản sp) chưa có voucher cho toàn bộ đơn hàng

        List<Order_Item> orderItems = new ArrayList<>();
        // Duyệt danh sách chuẩn bị tạo để chuyển thành dạng entity lưu database
        for (Order_Items_Create item : items) {
            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).get();
            if(productVariant == null) throw new AppException(ErrException.ORDER_ERROR_FIND_PRODUCT);
            Order_Item orderItem = new Order_Item();
            orderItem.setProductVariant(productVariant);
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(productVariant.getProduct().getPrice())
                    .multiply(BigDecimal.valueOf(productVariant.getProduct().getDiscount())
                            .divide(BigDecimal.valueOf(100))));
            orderItems.add(orderItem);

            // Cap nhat so luong da ban trong san pham
            int totalQuantity = productVariant.getProduct().getSoldQuantity();
            productVariant.getProduct().setSoldQuantity(totalQuantity + item.getQuantity());
            productRepository.save(productVariant.getProduct());

            // Tính toán luôn thuộc tính suy biến ở bảng order
            sum_before = sum_before.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotalAmount(sum_before);

        // tổng tổng giá trị của đơn hàng sau khi áp dụng voucher
        BigDecimal sum_after;
        if(order.getVoucher() != null && voucherService.checkVoucher(order.getVoucher().getCode())) sum_after = voucherService.applyVoucher(order.getVoucher(), sum_before);
        else sum_after = sum_before;
        order.setTotalPrice(sum_after);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setPaymentMethod(orderCreate.getPaymentMethod());
        order.setRecipientName(orderCreate.getRecipientName());
        order.setRecipientPhone(orderCreate.getRecipientPhone());
        order.setRecipientAddress(orderCreate.getRecipientAddress());
        order.setDescription(orderCreate.getDescription());
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return orderMapper.orderToOrderResponse(order);
    }


    // Cập nhật đơn hàng
    @Transactional
    @Override
    public OrderResponse updateOrder(Integer orderId, OrderUpdateUser orderUpdateUser)
    {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));

        // Kiểm tra trạng thái đơn hàng
        if (order.getPaymentStatus() == PaymentStatus.PAID || order.getStatus() == OrderStatus.DELIVERED) {
            throw new AppException(ErrException.NOT_UPDATE_ORDER);
        }

        // Cập nhật thông tin đơn hàng
        order.setPaymentMethod(orderUpdateUser.getPaymentMethod());
        order.setRecipientName(orderUpdateUser.getRecipientName());
        order.setRecipientPhone(orderUpdateUser.getRecipientPhone());
        order.setRecipientAddress(orderUpdateUser.getRecipientAddress());

        // Xóa các sản phẩm cũ
        List<Order_Item> dataEntity = order.getOrderItems();
        for (Order_Item temp : dataEntity) {
            int quantity = temp.getQuantity();
            int nowQuantity = temp.getProductVariant().getProduct().getSoldQuantity();
            temp.getProductVariant().getProduct().setSoldQuantity(nowQuantity - quantity);
            productRepository.save(temp.getProductVariant().getProduct());
        }
        dataEntity.clear(); // Xóa danh sách các sản phẩm cũ

        //
        List<Order_Items_Create> newDataRequest = orderUpdateUser.getOrderItems();
        BigDecimal sum_before = BigDecimal.valueOf(0);
        BigDecimal sum_after;
        for (Order_Items_Create item : newDataRequest) {
            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId()).get();
            Order_Item orderItem = new Order_Item();
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(productVariant.getProduct().getPrice())
                    .multiply(BigDecimal.valueOf(productVariant.getProduct().getDiscount())
                            .divide(BigDecimal.valueOf(100))));
            orderItem.setOrder(order);
            order.addOrderItem(orderItem);
            // Tính toán giá trị Total
            sum_before = sum_before.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            // Cap nhat so luong da ban
            int totalQuantity = productVariant.getProduct().getSoldQuantity();
            productVariant.getProduct().setSoldQuantity(totalQuantity + item.getQuantity());
            productRepository.save(productVariant.getProduct());
        }
        order.setTotalAmount(sum_before);
        if(orderUpdateUser.getCode() != null)
        {
            Voucher voucher = voucherRepository.findVoucherByCode(orderUpdateUser.getCode());
            if(voucher == null) throw new AppException(ErrException.VOUCHER_NOT_EXISTED);
            order.setVoucher(voucher);
        }else order.setVoucher(null);
        if(order.getVoucher() != null) sum_after = voucherService.applyVoucher(order.getVoucher(), sum_before);
        else sum_after = sum_before;
        order.setTotalPrice(sum_after);
        order.setDescription(orderUpdateUser.getDescription());
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        return orderMapper.orderToOrderResponse(order);
    }

    // Trong thực tế không nên xóa hẳn đơn hàng
    @Override
    public boolean deleteOrder(Integer orderId) {
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

    @Override
    public OrderResponse changeOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        if(status.equalsIgnoreCase(OrderStatus.SHIPPED.name())) order.setStatus(OrderStatus.SHIPPED);
        if(status.equalsIgnoreCase(OrderStatus.PENDING.name())) order.setStatus(OrderStatus.PENDING);
        if(status.equalsIgnoreCase(OrderStatus.DELIVERED.name())) order.setStatus(OrderStatus.DELIVERED);
        if(status.equalsIgnoreCase(OrderStatus.CANCELED.name())) order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderMapper.orderToOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse changePaymentStatus (Integer orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        if(paymentStatus.equalsIgnoreCase(PaymentStatus.PAID.name())) order.setPaymentStatus(PaymentStatus.PAID);
        if(paymentStatus.equalsIgnoreCase(PaymentStatus.UNPAID.name())) order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return orderMapper.orderToOrderResponse(orderRepository.save(order));
    }

    @Override
    public boolean cancelOrder (Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        List<Order_Item> orderItems = order.getOrderItems();
        for (Order_Item orderItem : orderItems) {
            int quantity  = orderItem.getProductVariant().getProduct().getSoldQuantity();
            orderItem.getProductVariant().getProduct().setSoldQuantity(quantity - orderItem.getQuantity());
            productRepository.save(orderItem.getProductVariant().getProduct());
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        return true;
    }

    @Override
    public ResultPaginationDTO getOrderByUserNameAndStatusOrder(String username, String status, Pageable pageable)
    {
        OrderSpecification spec =  new OrderSpecification(username, status, null);
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        ResultPaginationDTO re  = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        re.setMeta(mt);
        re.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
        return re;
    }

    @Override
    public ResultPaginationDTO getOrderByStatusOrder( String status, Pageable pageable)
    {
        OrderSpecification spec =  new OrderSpecification(null, status, null);
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        ResultPaginationDTO re  = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        re.setMeta(mt);
        re.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
        return re;
    }

    @Override
    public ResultPaginationDTO  getOrderByUserNameAndPaymentStatus(String username, String paymentStatus, Pageable pageable)
    {
        OrderSpecification spe =  new OrderSpecification(username, null, paymentStatus);
        Page<Order> orders = orderRepository.findAll(spe, pageable);
        ResultPaginationDTO re  = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        re.setMeta(mt);
        re.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
        return re;
    }

    @Override
    public ResultPaginationDTO getOrderByPaymentStatus(String payMentStatus, Pageable pageable)
    {
        OrderSpecification spe =  new OrderSpecification(null, null, payMentStatus);
        Page<Order> orders = orderRepository.findAll(spe, pageable);
        ResultPaginationDTO re  = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(orders.getTotalElements());
        mt.setPages(orders.getTotalPages());
        re.setMeta(mt);
        re.setResult(orderMapper.listOrderToOrderResponse(orders.getContent()));
        return re;
    }
}
