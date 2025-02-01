package com.bibek.service.order.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.OrderStatus;
import com.bibek.enums.PaymentStatus;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.*;
import com.bibek.repository.AddressRepository;
import com.bibek.repository.OrderItemRepository;
import com.bibek.repository.OrderRepository;
import com.bibek.service.order.OrderService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomMessageSource customMessageSource;

    public OrderServiceImpl(OrderRepository orderRepository, AddressRepository addressRepository, OrderItemRepository orderItemRepository, CustomMessageSource customMessageSource) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddress().contains(shippingAddress)){
            user.getAddress().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItems>> itemsBySeller = cart.getCartItems().stream().collect(Collectors.groupingBy(
                x -> x.getProduct()
                .getSeller().getId()));

        Set<Order> orders = new HashSet<>();
        for(Map.Entry<Long, List<CartItems>> entry: itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItems> items = entry.getValue();

            int totalOrderPrice = items.stream().mapToInt(
                    CartItems::getSellingPrice
            ).sum();

            int totalItem = items.stream().mapToInt(CartItems::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItems cartItems: items){
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(cartItems.getMrpPrice());
                orderItem.setProduct(cartItems.getProduct());
                orderItem.setQuantity(cartItems.getQuantity());
                orderItem.setSize(cartItems.getSize());orderItem.setUserId(cartItems.getUserId());
                orderItem.setSellingPrice(cartItems.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.NOT_EXIST, customMessageSource.get(MessageConstants.ORDER))));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getSellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        Order order = findOrderById(orderId);

        if(!user.getId() .equals(order.getUser().getId())){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.Access_Denied));
        }

            order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.ORDER_ITEM))));
    }
}
