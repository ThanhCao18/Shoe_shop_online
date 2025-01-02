package com.example.ananas.mapper;

import com.example.ananas.dto.response.CartItemResponse;
import com.example.ananas.entity.Cart_Item;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.ProductVariant;
import com.example.ananas.entity.Product_Image;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class ICartItemMapperImpl implements ICartItemMapper {

    @Override
    public CartItemResponse toCartItemResponse(Cart_Item cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponse.CartItemResponseBuilder cartItemResponse = CartItemResponse.builder();

        cartItemResponse.product( toProductVariantCart( cartItem.getProductVariant() ) );
        cartItemResponse.quantity( cartItem.getQuantity() );

        return cartItemResponse.build();
    }

    @Override
    public CartItemResponse.ProductVariantCart toProductVariantCart(ProductVariant productVariant) {
        if ( productVariant == null ) {
            return null;
        }

        CartItemResponse.ProductVariantCart productVariantCart = new CartItemResponse.ProductVariantCart();

        productVariantCart.setProductName( productVariantProductProductName( productVariant ) );
        productVariantCart.setProductId( productVariantProductProductId( productVariant ) );
        productVariantCart.setPrice( productVariantProductPrice( productVariant ) );
        productVariantCart.setVariantId( productVariant.getVariantId() );
        List<Product_Image> productImages = productVariantProductProductImages( productVariant );
        productVariantCart.setImages( ICartItemMapper.mapImages( productImages ) );
        productVariantCart.setColor( productVariant.getColor() );
        productVariantCart.setSize( productVariant.getSize() );
        productVariantCart.setStock( productVariant.getStock() );

        return productVariantCart;
    }

    @Override
    public List<CartItemResponse> toCartItemResponseList(List<Cart_Item> cartItemList) {
        if ( cartItemList == null ) {
            return null;
        }

        List<CartItemResponse> list = new ArrayList<CartItemResponse>( cartItemList.size() );
        for ( Cart_Item cart_Item : cartItemList ) {
            list.add( toCartItemResponse( cart_Item ) );
        }

        return list;
    }

    private String productVariantProductProductName(ProductVariant productVariant) {
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

    private int productVariantProductProductId(ProductVariant productVariant) {
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

    private double productVariantProductPrice(ProductVariant productVariant) {
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

    private List<Product_Image> productVariantProductProductImages(ProductVariant productVariant) {
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
