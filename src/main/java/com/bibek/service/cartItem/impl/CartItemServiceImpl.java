package com.bibek.service.cartItem.impl;


import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.CartItems;
import com.bibek.model.User;
import com.bibek.repository.CartItemRepository;
import com.bibek.service.cartItem.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CustomMessageSource customMessageSource;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CustomMessageSource customMessageSource) {
        this.cartItemRepository = cartItemRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public CartItems updateCartItems(Long userId, Long cartItemId, CartItems cartItems) {
        CartItems existCartItem = findCartItem(cartItemId);

        User user = existCartItem.getCart().getUser();
        if(user.getId().equals(userId)){
            existCartItem.setQuantity(cartItems.getQuantity());
            existCartItem.setMrpPrice(existCartItem.getQuantity() * existCartItem.getProduct().getMrpPrice());
            existCartItem.setSellingPrice(existCartItem.getSellingPrice());
            return cartItemRepository.save(existCartItem);

        } else {
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CANT_UPDATE, customMessageSource.get(MessageConstants.CART_ITEM)));
        }

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItems existCartItem = findCartItem(cartItemId);

        User user = existCartItem.getCart().getUser();

        if(user.getId().equals(userId)){
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.COULD_NOT_DELETE, customMessageSource.get(MessageConstants.CART_ITEM)));
        }
    }

    @Override
    public CartItems findCartItem(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.NOT_EXIST, customMessageSource.get(MessageConstants.CART_ITEM))));
    }
}
