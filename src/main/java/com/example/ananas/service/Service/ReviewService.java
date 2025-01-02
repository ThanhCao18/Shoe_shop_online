package com.example.ananas.service.Service;

import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Product;
import com.example.ananas.entity.Review;
import com.example.ananas.entity.User;
import com.example.ananas.mapper.IReviewMapper;
import com.example.ananas.repository.Product_Repository;
import com.example.ananas.repository.Review_Repository;
import com.example.ananas.repository.User_Repository;
import com.example.ananas.service.IService.IReviewService;
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
public class ReviewService implements IReviewService {
    Review_Repository reviewRepository;
    User_Repository userRepository;
    Product_Repository productRepository;
    IReviewMapper reviewMapper;

    public ReviewResponse addReviewToProduct(int user_id, int product_id, ReviewDTO review) {
        Review currentReview = new Review();

        // Kiểm tra user_id
        User user = userRepository.findById(user_id).orElseThrow(()->new RuntimeException("User Not Found"));

        // Kiểm tra product_id
        Product product = productRepository.findById(product_id).orElseThrow(()->new RuntimeException("Product ID " + product_id + " does not exist."));

        // Lấy thông tin product và user
        currentReview.setRating(review.getRating());
        currentReview.setComment(review.getComment());
        currentReview.setProductId(product.getProductId());
        currentReview.setUserId(user.getUserId());

        // Lưu review và trả về response
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

    @Override
    public List<Review> getAllReviewsByProductId(int product_id) {
        if (productRepository.existsById(product_id)) {
            return reviewRepository.findByProductId(product_id);
        }
        return null;
    }

    @Override
    public int getReviewsCountByProductId(int product_id) {
        List<Review> reviews = getAllReviewsByProductId(product_id);
        int count = 0;
        for (int i = 0; i < reviews.size(); i++) {
            count++;
        }
        return count;
    }

    @Override
    public double getAverageRatingByProductId(int product_id) {
        try {
            int count = getReviewsCountByProductId(product_id);
            List<Review> reviews = getAllReviewsByProductId(product_id);
            int sum = 0;
            for (int i = 0; i < reviews.size(); i++) {
                sum += reviews.get(i).getRating();
            }
            return sum / count;
        } catch (Exception e) {
            return 0;
        }
    }
}
