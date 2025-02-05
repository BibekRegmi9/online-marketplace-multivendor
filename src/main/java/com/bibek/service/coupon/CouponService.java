package com.bibek.service.coupon;

import com.bibek.model.Cart;
import com.bibek.model.Coupon;
import com.bibek.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, Double orderValue, User user);
    Cart removeCoupon(String couponCode, User user);
    Coupon byCouponById(Long id);
    Coupon createCoupon(Coupon coupon);
    List<Coupon> getAllCoupon();
    void deleteCoupon(Long couponId);
}
