package com.bibek.repository;

import com.bibek.model.Order;
import com.bibek.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
