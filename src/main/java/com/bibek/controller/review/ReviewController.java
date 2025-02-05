package com.bibek.controller.review;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.request.CreateReviewRequest;
import com.bibek.service.product.ProductService;
import com.bibek.service.review.ReviewService;
import com.bibek.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController extends BaseController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    private final CustomMessageSource customMessageSource;

    public ReviewController(ReviewService reviewService, UserService userService, ProductService productService, CustomMessageSource customMessageSource) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productService = productService;
        this.customMessageSource = customMessageSource;
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<GlobalApiResponse> getReviewByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.REVIEW)), reviewService.getReviewByProductId(productId)));
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<GlobalApiResponse> writeReview(@RequestBody CreateReviewRequest request,
                                                         @PathVariable Long productId,
                                                         @RequestHeader("Authorization") String jwt
                                                         ) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.REVIEW)), reviewService.createReview(request, userService.findUserByJwtToken(jwt), productService.findByProductId(productId))));
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<GlobalApiResponse> updateReview(@RequestBody CreateReviewRequest request,
                                                         @PathVariable Long reviewId,
                                                         @RequestHeader("Authorization") String jwt
    ) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_UPDATE, customMessageSource.get(MessageConstants.REVIEW)), reviewService.updateReview(reviewId, request.getReviewText(), request.getReviewRating(), userService.findUserByJwtToken(jwt).getId())));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<GlobalApiResponse> deleteReview(@PathVariable Long reviewId,
                                                          @RequestHeader("Authorization") String jwt
    ) {
        reviewService.deleteReview(reviewId, userService.findUserByJwtToken(jwt).getId());
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.REVIEW)), null));
    }
}
