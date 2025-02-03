package com.bibek.controller.sellerOrder;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.OrderStatus;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Order;
import com.bibek.model.Seller;
import com.bibek.model.User;
import com.bibek.service.order.OrderService;
import com.bibek.service.seller.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller/orders")
public class SellerOrderController extends BaseController {
    private final OrderService orderService;
    private final SellerService sellerService;
    private final CustomMessageSource customMessageSource;

    public SellerOrderController(OrderService orderService, SellerService sellerService, CustomMessageSource customMessageSource) {
        this.orderService = orderService;
        this.sellerService = sellerService;
        this.customMessageSource = customMessageSource;
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getAllOrders(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.getSellersOrder(seller.getId());

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), orders));
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<GlobalApiResponse> updateOrder(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long orderId,
                                                         @PathVariable OrderStatus orderStatus) {
        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.ORDER)), order));
    }
}
