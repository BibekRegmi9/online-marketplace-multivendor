package com.bibek.service.wishList;

import com.bibek.model.Product;
import com.bibek.model.User;
import com.bibek.model.WishList;

public interface WishListService {
    WishList createWishList(User user);
    WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user, Product product);
}
