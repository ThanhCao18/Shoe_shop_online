package com.example.ananas.mapper;

import com.example.ananas.dto.response.ProductImagesResponse;
import com.example.ananas.entity.Product;
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
public class IProductImageMapperImpl implements IProductImageMapper {

    @Override
    public ProductImagesResponse toProductImagesResponse(Product_Image productImage) {
        if ( productImage == null ) {
            return null;
        }

        ProductImagesResponse.ProductImagesResponseBuilder productImagesResponse = ProductImagesResponse.builder();

        productImagesResponse.imageUrl( productImage.getImageUrl() );
        productImagesResponse.productId( productImageProductProductId( productImage ) );
        productImagesResponse.id( productImage.getId() );

        return productImagesResponse.build();
    }

    @Override
    public List<ProductImagesResponse> toProductImagesResponseList(List<Product_Image> productImageList) {
        if ( productImageList == null ) {
            return null;
        }

        List<ProductImagesResponse> list = new ArrayList<ProductImagesResponse>( productImageList.size() );
        for ( Product_Image product_Image : productImageList ) {
            list.add( toProductImagesResponse( product_Image ) );
        }

        return list;
    }

    private int productImageProductProductId(Product_Image product_Image) {
        if ( product_Image == null ) {
            return 0;
        }
        Product product = product_Image.getProduct();
        if ( product == null ) {
            return 0;
        }
        int productId = product.getProductId();
        return productId;
    }
}
