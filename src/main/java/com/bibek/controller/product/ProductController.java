package com.bibek.controller.product;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.service.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final CustomMessageSource customMessageSource;

    public ProductController(ProductService productService, CustomMessageSource customMessageSource) {
        this.productService = productService;
        this.customMessageSource = customMessageSource;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.PRODUCT)), productService.findByProductId(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<GlobalApiResponse> searchProduct(@RequestParam(required = false) String query){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.PRODUCT)), productService.searchProduct(query)));
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getAllProduct(@RequestParam(required = false) String category,
                                                           @RequestParam(required = false) String brand,
                                                           @RequestParam(required = false) String color,
                                                           @RequestParam(required = false) String size,
                                                           @RequestParam(required = false) Integer minPrice,
                                                           @RequestParam(required = false) Integer maxPrice,
                                                           @RequestParam(required = false) Integer minDiscount,
                                                           @RequestParam(required = false) String sort,
                                                           @RequestParam(required = false) String stock,
                                                           @RequestParam(defaultValue = "0") Integer pageNumber
    ){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.PRODUCT)), productService.getAllProduct(
                category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber
        )));
    }
}
