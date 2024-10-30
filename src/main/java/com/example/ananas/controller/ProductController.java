package com.example.ananas.controller;

import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductImagesResponse;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.Product;
import com.example.ananas.exception.IdException;
import com.example.ananas.service.Service.ProductService;
import com.example.ananas.entity.ProductVariant;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ProductController {

    ProductService productService;


    @PostMapping("/product") //
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreateRequest));
    }

    //lay ra một ẩn pham
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponse> getOneProduct(@PathVariable int id) throws IdException
    {
        if(!this.productService.exisById(id))
        {
            throw new IdException("id sản phẩm không tồn tại");
        }
        return ResponseEntity.ok(productService.getOneProduct(id));
    }

    //lay ra toan bo san pham
    @GetMapping("/product")
    public ResponseEntity<ResultPaginationDTO> getAllProduct(@Filter Specification<Product> spec , Pageable pageable)
    {
        return ResponseEntity.ok(productService.getAllProduct(spec, pageable));
    }

    //cap nhat san pham va cac bien the cua no
    @PutMapping("product/{id}") // can sua tinh nang sua cac bien the cua san pham
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody ProductCreateRequest productCreateRequest) throws IdException
    {
        if(!this.productService.exisById(id))
            throw new IdException("id sản phẩm không tồn tại");
        return ResponseEntity.ok(productService.updateProduct(id,productCreateRequest));
    }

    //xoa san pham
    @DeleteMapping("product/{id}") //luu y khi xoa san pham can xoa tat ca cac bien the cua no truoc moi có the xoa duoc

    public ResponseEntity<String> deleteProduct(@PathVariable int id) throws IdException
    {
        if(!this.productService.exisById(id))
            throw new IdException("không tồn tại id sản phẩm");
        this.productService.deleteProduct(id);
        return ResponseEntity.ok("xóa thành công sản phẩm có id: "+id);
    }

    //them anh cho san pham
    @PutMapping("product/add_images/{id}")
    public ResponseEntity<String> addImages(@PathVariable int id, @RequestParam(name = "image")MultipartFile[] files) throws IdException, IOException {
        if(!this.productService.exisById(id))
            throw new IdException("id sản phẩm không tồn tại");
        this.productService.uploadImages(id,files);
        return ResponseEntity.ok("them thanh cong");
    }

    //lay ra toan bo anh cua san pham
    @GetMapping("/product/images/{id}")
    public ResponseEntity<List<ProductImagesResponse>> getAllImages(@PathVariable int id) throws IdException
    {
        if (!this.productService.exisById(id))
            throw new IdException("id sản phẩm không tồn tại");
        return ResponseEntity.ok(this.productService.getAllImages(id));
    }


    //phương thức xóa ảnh của sản phaam
    @DeleteMapping("product/images/{id}")
    public ResponseEntity<String> deleteImages(@PathVariable int id) throws IdException
    {
        if (!this.productService.exisById(id))
            throw new IdException("id ảnh không tồn tại");
        this.productService.deleteImages(id);

        return ResponseEntity.ok("xoa thành công");
    }

// các phương thức liên quan đến biến thể của sản phẩm
    //phương thức lấy ra tất cả các biến thể của sản phẩm
    @GetMapping("product/variants/{id}")
    public ResponseEntity<List<ProductVariant>> getAllProductvariants(@PathVariable int id) throws IdException
    {
        if(!this.productService.exisById(id))
        {
            throw new IdException("id sản phẩm không tồn tại");
        }
        return ResponseEntity.ok(this.productService.getAllProductVariants(id));
    }


}
