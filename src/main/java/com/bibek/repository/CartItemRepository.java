package com.bibek.repository;

import com.bibek.model.Cart;
import com.bibek.model.CartItems;
import com.bibek.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    CartItems findByCartAndProductAndSize(Cart cart, Product product, String size);
}
