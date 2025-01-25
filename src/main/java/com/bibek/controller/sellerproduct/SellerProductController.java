package com.bibek.controller.sellerproduct;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Product;
import com.bibek.model.Seller;
import com.bibek.request.CreateProductRequest;
import com.bibek.service.product.ProductService;
import com.bibek.service.seller.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller-product")
public class SellerProductController extends BaseController {
    private final SellerService sellerService;
    private final CustomMessageSource customMessageSource;
    private final ProductService productService;

    public SellerProductController(SellerService sellerService, CustomMessageSource customMessageSource, ProductService productService) {
        this.sellerService = sellerService;
        this.customMessageSource = customMessageSource;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getProductBySellerId(@RequestHeader("Authorization") String jwt){
        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.PRODUCT)), productService.getProductsBySellerId(seller.getId())));
    }

    @PostMapping
    public ResponseEntity<GlobalApiResponse> createProduct(@RequestBody CreateProductRequest request, @RequestHeader("Authorization") String jwt){
        Seller seller = sellerService.getSellerProfile(jwt);
        productService.createProduct(request, seller);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PRODUCT)), null));
    }

    @DeleteMapping
    public ResponseEntity<GlobalApiResponse> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.PRODUCT)), null));
    }

    @PutMapping
    public ResponseEntity<GlobalApiResponse> updateProduct(@PathVariable Long id, @RequestBody Product product){
        productService.updateProduct(id, product);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_UPDATE, customMessageSource.get(MessageConstants.PRODUCT)), null));
    }
}
