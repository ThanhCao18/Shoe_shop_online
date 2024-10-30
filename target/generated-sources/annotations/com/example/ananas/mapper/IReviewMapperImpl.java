package com.example.ananas.mapper;

import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Review;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
@Component
public class IReviewMapperImpl implements IReviewMapper {

    @Override
    public Review toReview(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( reviewDTO.getRating() );
        review.comment( reviewDTO.getComment() );

        return review.build();
    }

    @Override
    public ReviewDTO toReviewDTO(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setRating( review.getRating() );
        reviewDTO.setComment( review.getComment() );

        return reviewDTO;
    }

    @Override
    public ReviewResponse toReviewResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.reviewId( review.getReviewId() );
        reviewResponse.rating( review.getRating() );
        reviewResponse.comment( review.getComment() );
        reviewResponse.createdAt( review.getCreatedAt() );

        return reviewResponse.build();
    }

    @Override
    public void updateReview(Review review, ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return;
        }

        review.setRating( reviewDTO.getRating() );
        review.setComment( reviewDTO.getComment() );
    }
}
