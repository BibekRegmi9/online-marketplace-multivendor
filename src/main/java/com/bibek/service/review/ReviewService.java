package com.bibek.service.review;

import com.bibek.model.Product;
import com.bibek.model.Review;
import com.bibek.model.User;
import com.bibek.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService  {

    Review createReview(CreateReviewRequest request, User user, Product product);
    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, Double rating, Long userId);

    void deleteReview(Long reviewId, Long userId);

    Review getReviewById(Long id);
}
