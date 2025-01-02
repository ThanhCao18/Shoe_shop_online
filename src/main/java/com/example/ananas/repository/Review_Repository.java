package com.example.ananas.repository;

import com.example.ananas.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Review_Repository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(int productId);
}
