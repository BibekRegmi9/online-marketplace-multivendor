package com.bibek.controller.coupon;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Cart;
import com.bibek.model.Coupon;
import com.bibek.model.User;
import com.bibek.service.coupon.CouponService;
import com.bibek.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController {

    private final CouponService couponService;
    private final CustomMessageSource customMessageSource;
    private final UserService userService;

    public CouponController(CouponService couponService, CustomMessageSource customMessageSource, UserService userService) {
        this.couponService = couponService;
        this.customMessageSource = customMessageSource;
        this.userService = userService;
    }

    @PostMapping("/apply-coupon")
    public ResponseEntity<GlobalApiResponse> applyCoupon(@RequestHeader("Authorization") String jwt,
                                                         @RequestParam String apply,
                                                         @RequestParam String code,
                                                         @RequestParam double orderValue) {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if(apply.equals("true")){
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.COUPON)), cart));
    }

    @PostMapping("/admin-create")
    public ResponseEntity<GlobalApiResponse> createCoupon(@RequestBody Coupon cart) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.COUPON)), couponService.createCoupon(cart)));
    }

    @DeleteMapping("/admin-delete/{id}")
    public ResponseEntity<GlobalApiResponse> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.COUPON)), null));
    }

    @GetMapping("/admin/get-all")
    public ResponseEntity<GlobalApiResponse> getAllCoupon() {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.COUPON)), couponService.getAllCoupon()));
    }


}
