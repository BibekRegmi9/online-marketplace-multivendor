package com.bibek.controller.order;


import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.PaymentMethod;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.*;
import com.bibek.response.PaymentLinkResponse;
import com.bibek.service.cart.CartService;
import com.bibek.service.order.OrderService;
import com.bibek.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final CustomMessageSource customMessageSource;



    public OrderController(OrderService orderService, UserService userService, CartService cartService, CustomMessageSource customMessageSource) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.customMessageSource = customMessageSource;
    }

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(@RequestBody Address address,
                                                           @RequestBody PaymentMethod paymentMethod,
                                                           @RequestHeader("Authorization") String jwt){

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, address, cart);

        PaymentLinkResponse response = new PaymentLinkResponse();

//        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.PRODUCT)), response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<GlobalApiResponse> getUserOrderHistory(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.userOrderHistory(user.getId());

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GlobalApiResponse> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) {
//        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), order));
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<GlobalApiResponse> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization") String jwt) {
//        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), orderItem));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<GlobalApiResponse> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), order));
    }


}
