package com.example.ananas.mapper;

import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.entity.Category;
import com.example.ananas.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class IProductMapperImpl implements IProductMapper {

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.category( productCategoryCategoryName( product ) );
        productResponse.productId( product.getProductId() );
        productResponse.images( IProductMapper.mapImages( product.getProductImages() ) );
        productResponse.productName( product.getProductName() );
        productResponse.description( product.getDescription() );
        productResponse.price( product.getPrice() );
        productResponse.discount( product.getDiscount() );
        productResponse.material( product.getMaterial() );
        productResponse.soldQuantity( product.getSoldQuantity() );

        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toProductResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( products.size() );
        for ( Product product : products ) {
            list.add( toProductResponse( product ) );
        }

        return list;
    }

    @Override
    public Product toProduct(ProductCreateRequest productCreateRequest) {
        if ( productCreateRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.category( productCreateRequestToCategory( productCreateRequest ) );
        product.productName( productCreateRequest.getProductName() );
        product.description( productCreateRequest.getDescription() );
        product.price( productCreateRequest.getPrice() );
        product.discount( productCreateRequest.getDiscount() );
        product.material( productCreateRequest.getMaterial() );

        return product.build();
    }

    private String productCategoryCategoryName(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        String categoryName = category.getCategoryName();
        if ( categoryName == null ) {
            return null;
        }
        return categoryName;
    }

    protected Category productCreateRequestToCategory(ProductCreateRequest productCreateRequest) {
        if ( productCreateRequest == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.categoryName( productCreateRequest.getCategory() );

        return category.build();
    }
}
