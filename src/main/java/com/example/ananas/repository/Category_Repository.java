package com.example.ananas.repository;

import com.example.ananas.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Category_Repository extends JpaRepository<Category, Integer> {
    Category findByCategoryName(String name);
}
