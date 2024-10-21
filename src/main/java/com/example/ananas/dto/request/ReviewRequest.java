package com.example.ananas.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {
    int reviewId;
    int rating;
    String comment;
    Instant createdAt;
    int userId;
    int productId;
}
