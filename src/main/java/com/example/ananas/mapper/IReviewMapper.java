package com.example.ananas.mapper;

import com.example.ananas.dto.ReviewDTO;
import com.example.ananas.dto.response.ReviewResponse;
import com.example.ananas.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IReviewMapper {
    Review toReview(ReviewDTO reviewDTO);
    ReviewDTO toReviewDTO(Review review);
    ReviewResponse toReviewResponse(Review review);
    void updateReview(@MappingTarget Review review, ReviewDTO reviewDTO);
}
