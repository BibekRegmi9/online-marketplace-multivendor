package com.bibek.service.coupon.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Cart;
import com.bibek.model.Coupon;
import com.bibek.model.User;
import com.bibek.repository.CartRepository;
import com.bibek.repository.CouponRepository;
import com.bibek.repository.UserRepository;
import com.bibek.service.coupon.CouponService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final CustomMessageSource customMessageSource;
    private final UserRepository userRepository;

    public CouponServiceImpl(CouponRepository couponRepository, CartRepository cartRepository, CustomMessageSource customMessageSource, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
        this.customMessageSource = customMessageSource;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Cart applyCoupon(String code, Double orderValue, User user) {
        Coupon coupon = couponRepository.findByCode(code);
        Cart cart = cartRepository.findByUserId(user.getId());
        if(coupon == null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.COUPON)));
        }
        if(user.getUsedCoupons().contains(coupon)){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.ERROR_ALREADY_EXIST, customMessageSource.get(MessageConstants.COUPON)));
        }
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.COUPON_VALID));
        }
        if(coupon.isAsActive() && LocalDate.now().isAfter(coupon.getValidStartDate()) && LocalDate.now().isBefore(coupon.getValidEndDate())){
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }

        throw new CustomRunTimeException("Coupon not valid");

    }

    @Override
    public Cart removeCoupon(String couponCode, User user) {
        Coupon coupon = couponRepository.findByCode(couponCode);
        Cart cart = cartRepository.findByUserId(user.getId());
        if(coupon == null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.COUPON)));
        }

        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage()) / 100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon byCouponById(Long id) {
        return couponRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.COUPON))));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }
}
