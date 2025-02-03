package com.bibek.controller.wishList;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Seller;
import com.bibek.service.product.ProductService;
import com.bibek.service.user.UserService;
import com.bibek.service.wishList.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishListController extends BaseController {
    private final WishListService wishListService;
    private final CustomMessageSource customMessageSource;
    private final UserService userService;
    private final ProductService productService;

    public WishListController(WishListService wishListService, CustomMessageSource customMessageSource, UserService userService, ProductService productService) {
        this.wishListService = wishListService;
        this.customMessageSource = customMessageSource;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getWishListByUserId(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PAYMENT)), wishListService.getWishListByUserId(userService.findUserByJwtToken(jwt))));
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<GlobalApiResponse> addProductToWishList(@PathVariable Long productId, @RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PAYMENT)), wishListService.addProductToWishList(userService.findUserByJwtToken(jwt), productService.findByProductId(productId))));
    }

}
