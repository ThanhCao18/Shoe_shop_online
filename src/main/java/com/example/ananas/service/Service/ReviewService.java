package com.example.ananas.service.Service;

import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Review;
import com.example.ananas.mapper.IReviewMapper;
import com.example.ananas.repository.Product_Repository;
import com.example.ananas.repository.Review_Repository;
import com.example.ananas.repository.User_Repository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    Review_Repository reviewRepository;
    User_Repository userRepository;
    Product_Repository productRepository;
    IReviewMapper reviewMapper;

    public ReviewResponse addReviewToProduct(int user_id, int product_id, ReviewDTO review) {
        Review currentReview = new Review();
        if (userRepository.existsById(user_id)) {
            if (productRepository.existsById(product_id)) {
                currentReview.setRating(review.getRating());
                currentReview.setComment(review.getComment());
                currentReview.setProduct(productRepository.findById(product_id).get());
                currentReview.setUser(userRepository.findById(user_id).get());
            }
        }
        return reviewMapper.toReviewResponse(reviewRepository.save(currentReview));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll().stream().toList();
    }

    public Optional<ReviewResponse> getReviewById(int id) {
        return reviewRepository.findById(id).map(reviewMapper::toReviewResponse);
    }

    public void deleteReviewById(int id) {
        reviewRepository.deleteById(id);
    }

    public ReviewResponse updateReviewById(int id, ReviewDTO review) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            reviewMapper.updateReview(optionalReview.get(), review);
            review.setRating(review.getRating());
            review.setComment(review.getComment());
        }
        return reviewMapper.toReviewResponse(reviewRepository.save(optionalReview.get()));
    }
}
