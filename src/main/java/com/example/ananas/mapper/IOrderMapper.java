package com.example.ananas.mapper;

import com.example.ananas.dto.request.OrderCreate;
import com.example.ananas.dto.request.OrderUpdateUser;
import com.example.ananas.dto.request.Order_Items_Create;
import com.example.ananas.dto.response.OrderResponse;
import com.example.ananas.dto.response.Order_Item_Response;
import com.example.ananas.entity.Order_Item;
import com.example.ananas.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderMapper {

//
    List<OrderResponse> listOrderToOrderResponse (List<Order> orders);
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "voucher.code", target = "code")
    OrderResponse orderToOrderResponse(Order order);
    // Map Order_Item to Order_Item_Response
    List<Order_Item_Response> orderItemsToOrderItemResponses(List<Order_Item> orderItems);
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.description", target = "description")
    @Mapping(source = "product.price", target = "price_original")
    @Mapping(source = "product.discount", target = "discount")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "quantity", target = "quantity")
    Order_Item_Response orderItemToOrderItemResponse(Order_Item orderItem);
//

}
