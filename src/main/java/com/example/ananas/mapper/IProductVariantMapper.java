package com.example.ananas.mapper;

import com.example.ananas.dto.ProductVatriantDTO;
import com.example.ananas.entity.ProductVariant;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IProductVariantMapper {
//    @Mapping(source = "productId", target = "product.productId")
//    ProductVariant toProductVariant(ProductVatriantDTO productVatriantDTO);
}
