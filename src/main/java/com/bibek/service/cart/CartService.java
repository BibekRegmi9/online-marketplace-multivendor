package com.bibek.service.cart;

import com.bibek.model.Cart;
import com.bibek.model.CartItems;
import com.bibek.model.Product;
import com.bibek.model.User;

public interface CartService {
    CartItems addCartItems(User user,
                                  Product product,
                                  String size,
                                  int quality);

    Cart findUserCart(User user);
}
