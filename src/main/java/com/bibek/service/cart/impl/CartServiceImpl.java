package com.bibek.service.cart.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Cart;
import com.bibek.model.CartItems;
import com.bibek.model.Product;
import com.bibek.model.User;
import com.bibek.repository.CartItemRepository;
import com.bibek.repository.CartRepository;
import com.bibek.service.cart.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomMessageSource customMessageSource;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomMessageSource customMessageSource) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public CartItems addCartItems(User user, Product product, String size, int quality) {
        Cart cart = findUserCart(user);

        CartItems cartItems = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if(cartItems == null){
            CartItems newCartItem = new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quality);
            newCartItem.setUserId(user.getId());
            newCartItem.setCart(cart);
            newCartItem.setSize(size);

            int totalPrice = quality* product.getSellingPrice();
            newCartItem.setSellingPrice(totalPrice);
            newCartItem.setMrpPrice(quality * product.getMrpPrice());

            cart.getCartItems().add(newCartItem);
            newCartItem.setCart(cart);

            return cartItemRepository.save(newCartItem);
        }
        return cartItems;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalItem = 0 ;

        for(CartItems items: cart.getCartItems()){
            totalPrice += items.getMrpPrice();
            totalDiscountPrice += items.getSellingPrice();
            totalItem += items.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItems(totalItem);
        cart.setTotalSellingPrice(totalDiscountPrice);
        cart.setTotalItems(totalItem);
        cart.setDiscount(calculateDiscountPercent(totalPrice, totalDiscountPrice));
        return cart;
    }

    private int calculateDiscountPercent(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0){
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercent = (discount/mrpPrice)*100;
        return (int)discountPercent;
    }
}
