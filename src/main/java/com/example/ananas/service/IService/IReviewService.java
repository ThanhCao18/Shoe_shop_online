package com.example.ananas.service.IService;


import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    ReviewResponse addReviewToProduct(int user_id, int product_id, ReviewDTO review);
    List<Review> getAllReviews();
    Optional<ReviewResponse> getReviewById(int id);
    void deleteReviewById(int id);
    ReviewResponse updateReviewById(int id, ReviewDTO review);
    List<Review> getAllReviewsByProductId(int product_id);
    int getReviewsCountByProductId(int product_id);
    double getAverageRatingByProductId(int product_id);
}
