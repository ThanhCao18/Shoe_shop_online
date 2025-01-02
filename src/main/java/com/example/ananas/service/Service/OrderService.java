package com.example.ananas.service.Service;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.request.Order_Items_Create;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.*;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    Temp_Order_Repository tempOrderRepository;

    IOrderMapper orderMapper;
    Cart_Repository cartRepository;
    Cart_Item_Repository cartItemRepository;
    EmailService emailService;
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
    public ResultPaginationDTO getOrderByUserId(Integer userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
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
    public OrderResponse getOrderByOrderId(Integer orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        if(order == null) {
            throw new AppException(ErrException.ORDER_NOT_EXISTED);
        }
        return orderMapper.orderToOrderResponse(order);
    }


    @Override
    public BigDecimal getSumByDay(String date) {
        return orderRepository.getTotalOrderAmountByDate(date);
    }

    @Override
    public BigDecimal getSumByMonth(int month, int year) {
        return orderRepository.getTotalOrderAmountByMonth(month, year);
    }

    @Override
    public BigDecimal getSumByYear(int year) {
        return orderRepository.getTotalOrderAmountByYear(year);
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
            orderItem.setPrice(BigDecimal.valueOf(productVariant.getProduct().getPrice()).subtract(
                    BigDecimal.valueOf(productVariant.getProduct().getPrice())
                            .multiply(BigDecimal.valueOf(productVariant.getProduct().getDiscount())
                                    .divide(BigDecimal.valueOf(100))))
                    );
            orderItems.add(orderItem);

            // Cap nhat so luong da ban trong san pham
            int totalQuantity = productVariant.getProduct().getSoldQuantity();
            productVariant.getProduct().setSoldQuantity(totalQuantity + item.getQuantity());
            productRepository.save(productVariant.getProduct());

            // Cap nhat stock
            int stock = productVariant.getStock();
            if(stock < item.getQuantity()) throw new AppException(ErrException.NOT_ENOUGH_STOCK);
            productVariant.setStock(stock - item.getQuantity());
            productVariantRepository.save(productVariant);

            // Tính toán luôn thuộc tính suy biến ở bảng order
            sum_before = sum_before.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }
        order.setTotalAmount(sum_before);

        // tổng tổng giá trị của đơn hàng sau khi áp dụng voucher
        BigDecimal sum_after;
        StringBuilder description = new StringBuilder();
        if(order.getVoucher() != null && voucherService.checkVoucher(order.getVoucher().getCode(), sum_before))
        {
            BigDecimal discount_vourcher =  voucherService.applyVoucher(order.getVoucher(), sum_before);
            Integer vou = discount_vourcher.setScale(0, RoundingMode.HALF_EVEN).intValue();
            order.setDiscount_voucher(BigDecimal.valueOf(vou));
            sum_after = sum_before.subtract(BigDecimal.valueOf(vou));
            order.getVoucher().setUsageLimit(order.getVoucher().getUsageLimit()-1);
            voucherRepository.save(order.getVoucher());
        }
        else
        {
            order.setVoucher(null);
            sum_after = sum_before;
        }
        description.append(orderCreate.getDescription());
        order.setDescription(description.toString());
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

        // Update `saleAt` if status is SHIPPED or DELIVERED
       // if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            for (Order_Item orderItem : orderItems) {
                Product product = orderItem.getProductVariant().getProduct();
                product.setSaleAt(LocalDateTime.now());
                productRepository.save(product);
            }
       // }

        Optional<User> user = userRepository.findById(userId);

        if (user.get().getEmail() != null && !user.get().getEmail().isEmpty()) {
            String subject = "You have placed your order successfully";
            String text = "\nDear "+user.get().getUsername()+"\nYour order is being prepared.Thank you for your order <3";
            emailService.sendMessage(user.get().getEmail(), subject, text);
        }
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

            int stock = temp.getProductVariant().getStock();
            temp.getProductVariant().setStock(stock + temp.getQuantity());
            productVariantRepository.save(temp.getProductVariant());
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

            // Cap nhat stock
            int stock = productVariant.getStock();
            if(stock < item.getQuantity()) throw new AppException(ErrException.NOT_ENOUGH_STOCK);
            productVariant.setStock(stock - item.getQuantity());
            productVariantRepository.save(productVariant);
        }
        if(order.getVoucher() != null)
        {
            order.getVoucher().setUsageLimit(order.getVoucher().getUsageLimit()+1);
            voucherRepository.save(order.getVoucher());
        }
        order.setTotalAmount(sum_before);
        if(orderUpdateUser.getCode() != null)
        {
            Voucher voucher = voucherRepository.findVoucherByCode(orderUpdateUser.getCode());
            if(voucher == null) throw new AppException(ErrException.VOUCHER_NOT_EXISTED);
            order.setVoucher(voucher);
        }else order.setVoucher(null);
        StringBuilder description = new StringBuilder();
        if(order.getVoucher() != null && voucherService.checkVoucher(order.getVoucher().getCode(), sum_before))
        {
            BigDecimal dis_vour = voucherService.applyVoucher(order.getVoucher(), sum_before);
            Integer vou = dis_vour.setScale(0, RoundingMode.HALF_EVEN).intValue();
            order.setDiscount_voucher(BigDecimal.valueOf(vou));
            sum_after = sum_before.subtract(BigDecimal.valueOf(vou));
            order.getVoucher().setUsageLimit(order.getVoucher().getUsageLimit()-1);
            voucherRepository.save(order.getVoucher());
        }
        else
        {
            order.setVoucher(null);
            order.setDiscount_voucher(null);
            sum_after = sum_before;
        }
        order.setTotalPrice(sum_after);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        description.append(orderUpdateUser.getDescription());
        order.setDescription(description.toString());
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

    private void updateProductSaleAt(Order order) {
        List<Order_Item> orderItems = order.getOrderItems(); // Assuming this fetches items in the order
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        for (Order_Item item : orderItems) {
            ProductVariant productVariant = item.getProductVariant(); // Assuming this gets the associated ProductVariant
            Product product = productVariant.getProduct(); // Access the associated Product
            product.setSaleAt(LocalDateTime.now());
            productRepository.save(product); // Save the updated Product entity
        }
    }

    private void resetProductSaleAt(Order order) {
        List<Order_Item> orderItems = order.getOrderItems();

        for (Order_Item orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            Product product = productVariant.getProduct();

            // Đặt lại giá trị saleAt về null hoặc giá trị mặc định
            product.setSaleAt(LocalDateTime.from(product.getCreatedAt())); // hoặc đặt giá trị khác nếu cần
            productRepository.save(product);
        }
    }


    @Override
    public OrderResponse changeOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrException.ORDER_NOT_EXISTED));
        if(order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
            throw new AppException(ErrException.ORDER_ERROR_STATUS);
        }
        if(status.equalsIgnoreCase(OrderStatus.SHIPPED.name())) {
            order.setStatus(OrderStatus.SHIPPED);
            updateProductSaleAt(order);
        }
        if(status.equalsIgnoreCase(OrderStatus.PENDING.name())) order.setStatus(OrderStatus.PENDING);
        if(status.equalsIgnoreCase(OrderStatus.DELIVERED.name())) {
            order.setStatus(OrderStatus.DELIVERED);
            updateProductSaleAt(order);
        }
        if(status.equalsIgnoreCase(OrderStatus.CANCELED.name()))
        {
            order.setStatus(OrderStatus.CANCELED);
            resetProductSaleAt(order);
        }
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
        if(paymentStatus.equalsIgnoreCase(PaymentStatus.PAID.name())) {
            order.setPaymentStatus(PaymentStatus.PAID);
            updateProductSaleAt(order);
        }
        if(paymentStatus.equalsIgnoreCase(PaymentStatus.UNPAID.name())) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
            resetProductSaleAt(order);
        }
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

            int stock = orderItem.getProductVariant().getStock();
            orderItem.getProductVariant().setStock(stock + orderItem.getQuantity());
            productVariantRepository.save(orderItem.getProductVariant());
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        resetProductSaleAt(order);
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

    @Override
    @Transactional
    public void handleAfterCreateOrder(int orderId) {
        Order order = this.orderRepository.findByOrderId(orderId);
        User user = order.getUser();
        Cart cartDelete = this.cartRepository.findByUser(user);
        this.cartItemRepository.deleteByCart(cartDelete);
        this.cartRepository.deleteByUser(user);
    }

    @Override
    public ResultPaginationDTO getAllTempOrder(Specification<TempOrder> spec, Pageable pageable) {
        Page<TempOrder> tempOrderPage = this.tempOrderRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(tempOrderPage.getTotalPages());
        mt.setTotal(tempOrderPage.getTotalElements());

        rs.setMeta(mt);
        List<TempOrder> tempOrderList = tempOrderPage.getContent();
        rs.setResult(tempOrderList);
        return rs;
    }

    @Override
    public List<TempOrder> getAllTemp() {
      return   this.tempOrderRepository.findAll();
    }

    @Override
    public Order findOrderByOrderId(Integer orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}
