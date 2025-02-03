package com.bibek.service.wishList.impl;

import com.bibek.model.Product;
import com.bibek.model.User;
import com.bibek.model.WishList;
import com.bibek.repository.WishListRepository;
import com.bibek.service.wishList.WishListService;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishList createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
        WishList wishList = wishListRepository.findByUserId(user.getId());
        if(wishList == null){
            wishList = createWishList(user);
        }
        return wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList = getWishListByUserId(user);
        if(wishList.getProducts().contains(product)){
            wishList.getProducts().remove(product);
        } else {
            wishList.getProducts().add(product);
        }
        return wishListRepository.save(wishList);
    }
}
