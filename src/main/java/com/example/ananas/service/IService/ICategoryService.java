package com.example.ananas.service.IService;

import com.example.ananas.dto.request.CategoryCreateRequest;
import com.example.ananas.dto.response.CategoryResponse;
import com.example.ananas.entity.Category;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryResponse updateCategory(CategoryCreateRequest category, int id);
    Boolean deleteCategory(int id);
    Category getOneCategory(int id);
    Boolean exisById(int id);
    List<Category> getAllCategory();
}
