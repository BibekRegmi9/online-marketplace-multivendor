package com.bibek.service.cartItem;

import com.bibek.model.CartItems;

public interface CartItemService {
    CartItems updateCartItems(Long userId, Long cartItemId, CartItems cartItems);
    void removeCartItem(Long userId, Long cartItemId);
    CartItems findCartItem(Long id);
}
