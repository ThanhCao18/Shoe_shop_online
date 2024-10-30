package com.example.ananas.repository;

import com.example.ananas.entity.Product;
import com.example.ananas.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariant_Repository extends JpaRepository<ProductVariant,Integer> {
    void deleteProductVariantsByProduct(Product product);
    List<ProductVariant> findProductVariantsByProduct(Product product);
//    ProductVariant findProductVariantsByProductAndColorAndSize(Product product,String color,int size);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product = :product AND pv.color = :color AND pv.size = :size")
    ProductVariant findProductVariantByProductAndColorAndSize(
            @Param("product") Product product,
            @Param("color") String color,
            @Param("size") int size);


//@Query("SELECT pv FROM ProductVariant pv WHERE pv.product = :product AND pv.color = :color AND pv.size = :size")
//Optional<ProductVariant> findByProductColorAndSize(
//        @Param("product") Product product,
//        @Param("color") String color,
//        @Param("size") int size);
//}

    @Query(value = "SELECT * FROM product_variant WHERE product_id = :productId AND color = :color AND size = :size", nativeQuery = true)
    ProductVariant findByProductColorAndSize(
            @Param("productId") int productId,
            @Param("color") String color,
            @Param("size") int size);
}
