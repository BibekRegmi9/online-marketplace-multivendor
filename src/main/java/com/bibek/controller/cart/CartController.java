package com.bibek.controller.cart;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Cart;
import com.bibek.model.CartItems;
import com.bibek.model.Product;
import com.bibek.model.User;
import com.bibek.request.AddItemRequest;
import com.bibek.response.UserResponse;
import com.bibek.service.cart.CartService;
import com.bibek.service.cartItem.CartItemService;
import com.bibek.service.product.ProductService;
import com.bibek.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final CustomMessageSource customMessageSource;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService, CartItemService cartItemService, CustomMessageSource customMessageSource, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.customMessageSource = customMessageSource;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> findUserCart(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.CART_ITEM)), cart));
    }

    @PutMapping("/add")
    public ResponseEntity<GlobalApiResponse> addItemToCart(@RequestBody AddItemRequest request,
                                                           @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findByProductId(request.getProductId());

        CartItems cartItems = cartService.addCartItems(user, product, request.getSize(), request.getQuantity());

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.CART_ITEM)), cartItems));

    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<GlobalApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                            @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.CART_ITEM)), null));
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<GlobalApiResponse> updateCartItem(@PathVariable Long cartItemId,
                                                            @RequestBody CartItems cartItems,
                                                            @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwtToken(jwt);

        CartItems updateCartItem = null;
        if(cartItems.getQuantity() > 0){
            updateCartItem = cartItemService.updateCartItems(user.getId(), cartItemId, cartItems);
        }

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_UPDATE, customMessageSource.get(MessageConstants.CART_ITEM)), updateCartItem));
    }
}
