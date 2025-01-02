package com.example.ananas.mapper;

import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.Order_Item_Response;
import com.example.ananas.entity.Order_Item;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.ProductVariant;
import com.example.ananas.entity.Product_Image;
import com.example.ananas.entity.User;
import com.example.ananas.entity.order.Order;
import com.example.ananas.entity.voucher.Voucher;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IOrderMapperImpl implements IOrderMapper {

    @Override
    public List<OrderResponse> listOrderToOrderResponse(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderResponse> list = new ArrayList<OrderResponse>( orders.size() );
        for ( Order order : orders ) {
            list.add( orderToOrderResponse( order ) );
        }

        return list;
    }

    @Override
    public OrderResponse orderToOrderResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setUserId( orderUserUserId( order ) );
        orderResponse.setCode( orderVoucherCode( order ) );
        orderResponse.setId( order.getId() );
        orderResponse.setDescription( order.getDescription() );
        orderResponse.setDiscount_voucher( order.getDiscount_voucher() );
        orderResponse.setTotalAmount( order.getTotalAmount() );
        orderResponse.setTotalPrice( order.getTotalPrice() );
        orderResponse.setStatus( order.getStatus() );
        orderResponse.setPaymentMethod( order.getPaymentMethod() );
        orderResponse.setPaymentStatus( order.getPaymentStatus() );
        orderResponse.setRecipientName( order.getRecipientName() );
        orderResponse.setRecipientPhone( order.getRecipientPhone() );
        orderResponse.setRecipientAddress( order.getRecipientAddress() );
        orderResponse.setCreatedAt( order.getCreatedAt() );
        orderResponse.setUpdatedAt( order.getUpdatedAt() );
        orderResponse.setOrderItems( orderItemsToOrderItemResponses( order.getOrderItems() ) );

        return orderResponse;
    }

    @Override
    public List<Order_Item_Response> orderItemsToOrderItemResponses(List<Order_Item> orderItems) {
        if ( orderItems == null ) {
            return null;
        }

        List<Order_Item_Response> list = new ArrayList<Order_Item_Response>( orderItems.size() );
        for ( Order_Item order_Item : orderItems ) {
            list.add( orderItemToOrderItemResponse( order_Item ) );
        }

        return list;
    }

    @Override
    public Order_Item_Response orderItemToOrderItemResponse(Order_Item orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        Order_Item_Response order_Item_Response = new Order_Item_Response();

        order_Item_Response.setProductVariantId( orderItemProductVariantVariantId( orderItem ) );
        order_Item_Response.setProductId( orderItemProductVariantProductProductId( orderItem ) );
        order_Item_Response.setProductName( orderItemProductVariantProductProductName( orderItem ) );
        order_Item_Response.setDescription( orderItemProductVariantProductDescription( orderItem ) );
        order_Item_Response.setPrice_original( orderItemProductVariantProductPrice( orderItem ) );
        order_Item_Response.setDiscount( orderItemProductVariantProductDiscount( orderItem ) );
        order_Item_Response.setPrice( orderItem.getPrice() );
        order_Item_Response.setSize( orderItemProductVariantSize( orderItem ) );
        order_Item_Response.setColor( orderItemProductVariantColor( orderItem ) );
        order_Item_Response.setImage( mapProductImagesToFirstImageUrl( orderItemProductVariantProductProductImages( orderItem ) ) );
        order_Item_Response.setQuantity( orderItem.getQuantity() );

        return order_Item_Response;
    }

    private int orderUserUserId(Order order) {
        if ( order == null ) {
            return 0;
        }
        User user = order.getUser();
        if ( user == null ) {
            return 0;
        }
        int userId = user.getUserId();
        return userId;
    }

    private String orderVoucherCode(Order order) {
        if ( order == null ) {
            return null;
        }
        Voucher voucher = order.getVoucher();
        if ( voucher == null ) {
            return null;
        }
        String code = voucher.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private int orderItemProductVariantVariantId(Order_Item order_Item) {
        if ( order_Item == null ) {
            return 0;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return 0;
        }
        int variantId = productVariant.getVariantId();
        return variantId;
    }

    private int orderItemProductVariantProductProductId(Order_Item order_Item) {
        if ( order_Item == null ) {
            return 0;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return 0;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return 0;
        }
        int productId = product.getProductId();
        return productId;
    }

    private String orderItemProductVariantProductProductName(Order_Item order_Item) {
        if ( order_Item == null ) {
            return null;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        String productName = product.getProductName();
        if ( productName == null ) {
            return null;
        }
        return productName;
    }

    private String orderItemProductVariantProductDescription(Order_Item order_Item) {
        if ( order_Item == null ) {
            return null;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        String description = product.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private double orderItemProductVariantProductPrice(Order_Item order_Item) {
        if ( order_Item == null ) {
            return 0.0d;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return 0.0d;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return 0.0d;
        }
        double price = product.getPrice();
        return price;
    }

    private double orderItemProductVariantProductDiscount(Order_Item order_Item) {
        if ( order_Item == null ) {
            return 0.0d;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return 0.0d;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return 0.0d;
        }
        double discount = product.getDiscount();
        return discount;
    }

    private int orderItemProductVariantSize(Order_Item order_Item) {
        if ( order_Item == null ) {
            return 0;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return 0;
        }
        int size = productVariant.getSize();
        return size;
    }

    private String orderItemProductVariantColor(Order_Item order_Item) {
        if ( order_Item == null ) {
            return null;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        String color = productVariant.getColor();
        if ( color == null ) {
            return null;
        }
        return color;
    }

    private List<Product_Image> orderItemProductVariantProductProductImages(Order_Item order_Item) {
        if ( order_Item == null ) {
            return null;
        }
        ProductVariant productVariant = order_Item.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        List<Product_Image> productImages = product.getProductImages();
        if ( productImages == null ) {
            return null;
        }
        return productImages;
    }
}
