package com.example.ananas.service.Service;

import com.example.ananas.dto.ProductVatriantDTO;
import com.example.ananas.dto.request.ProductCreateRequest;
import com.example.ananas.dto.response.ProductImagesResponse;
import com.example.ananas.dto.response.ProductResponse;
import com.example.ananas.dto.response.ResultPaginationDTO;
import com.example.ananas.entity.Category;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.ProductVariant;
import com.example.ananas.entity.Product_Image;
import com.example.ananas.exception.IdException;
import com.example.ananas.mapper.IProductImageMapper;
import com.example.ananas.mapper.IProductMapper;
import com.example.ananas.mapper.IProductVariantMapper;
import com.example.ananas.repository.Category_Repository;
import com.example.ananas.repository.ProductVariant_Repository;
import com.example.ananas.repository.Product_Image_Repository;
import com.example.ananas.repository.Product_Repository;
import com.example.ananas.service.IService.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {

    private static final String UPLOAD_DIR = "upload/";
    Product_Repository productRepository;
    Product_Image_Repository productImageRepository;
    Category_Repository categoryRepository;
    IProductMapper productMapper;
    IProductImageMapper productImageMapper;
    ProductVariant_Repository productVariantRepository;
    IProductVariantMapper productVariantMapper;
    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {

        Product createProduct = this.productMapper.toProduct(productCreateRequest);
        Category category = this.categoryRepository.findByCategoryName(productCreateRequest.getCategory());
        createProduct.setCategory(category);

        Product product =  this.productRepository.save(createProduct);
        List<ProductVatriantDTO> productVatriantDTOList = productCreateRequest.getVariants();
        productVatriantDTOList.stream().forEach(item ->{
            ProductVariant productVariant = new ProductVariant();
            productVariant.setSize(item.getSize());
            productVariant.setColor(item.getColor());
            productVariant.setStock(item.getStock());
            productVariant.setProduct(product);
            this.productVariantRepository.save(productVariant);
        });

        return this.productMapper.toProductResponse(createProduct);
    }

    @Override
    public ProductResponse getOneProduct(int id) {

        Product product = this.productRepository.findById(id).get();
        return productMapper.toProductResponse(product);
    }

    @Override
    public ResultPaginationDTO getAllProduct(Specification<Product> spec, Pageable pageable) {
        Page<Product> productPage = this.productRepository.findAll(spec,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(productPage.getTotalPages());
        mt.setTotal(productPage.getTotalElements());

        rs.setMeta(mt);
        List<Product> productList = productPage.getContent();
        List<ProductResponse> productResponseList = this.productMapper.toProductResponseList(productPage.getContent());
        rs.setResult(productResponseList);

        return rs;

    }


    @Override
    public ProductResponse updateProduct(int id, ProductCreateRequest productCreateRequest) {
        Product product = this.productRepository.findById(id).get();
        Product updateProduct = this.productMapper.toProduct(productCreateRequest);
        product.setProductName(productCreateRequest.getProductName());
        Category updateCategory = this.categoryRepository.findByCategoryName(productCreateRequest.getCategory());
        product.setCategory(updateCategory);
        product.setDescription(productCreateRequest.getDescription());
        product.setDiscount(productCreateRequest.getDiscount());
        product.setMaterial(productCreateRequest.getMaterial());
        product.setPrice(productCreateRequest.getPrice());
        this.productRepository.save(product);


        List<ProductVatriantDTO> productVatriantDTOList = productCreateRequest.getVariants();
        productVatriantDTOList.stream().forEach(item ->{
            ProductVariant productVariant = new ProductVariant();
            productVariant.setSize(item.getSize());
            productVariant.setColor(item.getColor());
            productVariant.setStock(item.getStock());
            productVariant.setProduct(product);
            this.productVariantRepository.save(productVariant);
        });
        return this.productMapper.toProductResponse(product);

    }

    @Override
    public boolean exisById(int id) {
        return this.productRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteProduct(int id)  {
//        List<ProductVariant> productVariants = this.productVariantRepository.findProductVariantsByProduct(this.productRepository.findById(id).get());
//        if (productVariants.size() != 0)
//            this.productVariantRepository.deleteProductVariantsByProduct(this.productRepository.findById(id).get());

        this.productRepository.deleteById(id);
    }

    @Override
    public void uploadImages(int id, MultipartFile[] files) throws IOException {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Kiểm tra và tạo thư mục lưu trữ nếu chưa có
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            // Lưu từng file ảnh
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Lưu thông tin ảnh vào database
            Product_Image image = new Product_Image();
            image.setImageUrl(filePath.toString());
            image.setProduct(product);
            this.productImageRepository.save(image);
        }
    }


    @Override
    public List<ProductImagesResponse> getAllImages(int id) {
        Product product = this.productRepository.findById(id).get();
        List<Product_Image> list = this.productImageRepository.findAllByProduct(product);
        return this.productImageMapper.toProductImagesResponseList(list);
    }

    @Override
    public void deleteImages(int id) {
        this.productImageRepository.deleteById(id);
    }

    @Override
    public List<ProductVariant> getAllProductVariants(int id) {

        return this.productVariantRepository.findProductVariantsByProduct(this.productRepository.findById(id).get());
    }
}
