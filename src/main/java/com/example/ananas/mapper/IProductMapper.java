package com.example.ananas.mapper;

import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.Product_Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    @Mapping(source = "category.categoryName", target = "category")
    @Mapping(source = "productId",target = "productId")
    @Mapping(target = "images", source = "productImages", qualifiedByName = "mapImages")
    ProductResponse toProductResponse(Product product);

    // Hàm ánh xạ từng `Product_Image` thành `String`
    @Named("mapImages")
    static List<String> mapImages(List<Product_Image> productImages) {
        return productImages != null ?
                productImages.stream().map(Product_Image::getImageUrl).collect(Collectors.toList()) :
                null;
    }



    List<ProductResponse> toProductResponseList(List<Product> products);
    @Mapping(target = "category.categoryName", source = "category")
    Product toProduct(ProductCreateRequest productCreateRequest);

}
