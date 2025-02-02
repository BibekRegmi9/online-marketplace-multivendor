package com.bibek.service.order;

import com.bibek.enums.OrderStatus;
import com.bibek.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

    Order findOrderById(Long id);

    List<Order> userOrderHistory(Long userId);

    List<Order> getSellersOrder(Long sellerId);

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);

    Order cancelOrder(Long orderId, User user);

    OrderItem getOrderItemById(Long id);

}
