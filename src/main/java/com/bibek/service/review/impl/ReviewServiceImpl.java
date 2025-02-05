package com.bibek.service.review.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Product;
import com.bibek.model.Review;
import com.bibek.model.User;
import com.bibek.repository.ReviewRepository;
import com.bibek.request.CreateReviewRequest;
import com.bibek.service.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomMessageSource customMessageSource;

    public ReviewServiceImpl(ReviewRepository repository, ReviewRepository reviewRepository, CustomMessageSource customMessageSource) {
        this.reviewRepository = reviewRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public Review createReview(CreateReviewRequest request, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(request.getReviewText());
        review.setRating(request.getReviewRating());
        review.setProductImages(request.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, Double rating, Long userId) {
        Review review = getReviewById(reviewId);

        if(review.getUser().getId().equals(userId)){
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CANT_UPDATE, customMessageSource.get(MessageConstants.REVIEW)));
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CANT_DELETE, customMessageSource.get(MessageConstants.REVIEW)));
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.REVIEW))));
    }
}
