package com.example.ananas.mapper;

import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    @Mapping(source = "category.categoryName", target = "category")
    @Mapping(source = "productId",target = "productId")
    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);
    @Mapping(target = "category.categoryName", source = "category")
    Product toProduct(ProductCreateRequest productCreateRequest);

}
