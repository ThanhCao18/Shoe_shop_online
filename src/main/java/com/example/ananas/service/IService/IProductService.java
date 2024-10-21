package com.example.ananas.service.IService;

import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductImagesResponse;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    ProductCreateRequest createProduct(ProductCreateRequest productCreateRequest);

    ProductResponse getOneProduct(int id);

    ResultPaginationDTO getAllProduct(Specification<Product> spec, Pageable pageable);

    ProductResponse updateProduct(int id, ProductCreateRequest productCreateRequest);

    boolean exisById(int id);

    void deleteProduct(int id);

    void uploadImages(int id, MultipartFile[]  files) throws IOException;

    List<ProductImagesResponse> getAllImages(int id);

    void deleteImages(int id);
}
