package com.example.ananas.controller;

import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.request.ReviewRequest;
import com.example.ananas.dto.response.ApiResponse;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Review;
import com.example.ananas.service.Service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/review")
public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponse> addReview(@RequestParam int userId, @RequestParam int productId, @RequestBody ReviewDTO review) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.addReviewToProduct(userId, productId, review))
                .code(200)
                .build();

    }


    @GetMapping
    public ApiResponse<List<Review>> getAllReview() {
        return ApiResponse.<List<Review>>builder()
                .result(reviewService.getAllReviews())
                .code(200)
                .build();
    }


    @DeleteMapping
    public ApiResponse<Void> deleteReviewById(@RequestParam int reviewId) {
        reviewService.deleteReviewById(reviewId);
        return null;
    }

    @PutMapping
    public ApiResponse<ReviewResponse> updateReviewById(@RequestParam int reviewId, @RequestBody ReviewDTO review) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReviewById(reviewId, review))
                .code(200)
                .build();
    }


}
