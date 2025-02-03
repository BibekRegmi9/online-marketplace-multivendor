package com.bibek.controller.order;


import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.PaymentMethod;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.*;
import com.bibek.repository.PaymentOrderRepository;
import com.bibek.response.PaymentLinkResponse;
import com.bibek.service.cart.CartService;
import com.bibek.service.order.OrderService;
import com.bibek.service.payment.PaymentService;
import com.bibek.service.seller.SellerService;
import com.bibek.service.sellerReport.SellerReportService;
import com.bibek.service.user.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
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
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;


    public OrderController(OrderService orderService, UserService userService, CartService cartService, CustomMessageSource customMessageSource, SellerService sellerService, SellerReportService sellerReportService, PaymentService paymentService, PaymentOrderRepository paymentOrderRepository) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.customMessageSource = customMessageSource;
        this.sellerService = sellerService;
        this.sellerReportService = sellerReportService;
        this.paymentService = paymentService;
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @PostMapping
    public ResponseEntity<GlobalApiResponse> createOrder(@RequestBody Address address,
                                                           @RequestBody PaymentMethod paymentMethod,
                                                           @RequestHeader("Authorization") String jwt) throws RazorpayException, StripeException {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, address, cart);
        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse response = new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink paymentLink = paymentService.createRazorPayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");

            response.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        } else {
            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            response.setGetPayment_link_id(paymentUrl);

        }
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.ORDER)), response));
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

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        sellerReport.setCancelledOrders(sellerReport.getCancelledOrders() + 1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), order));
    }


}
