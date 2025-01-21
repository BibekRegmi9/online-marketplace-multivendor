package com.bibek.service.product;

import com.bibek.model.Product;
import com.bibek.model.Seller;
import com.bibek.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest productReq, Seller seller);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product product);
    Product findByProductId(Long id);
    List<Product> searchProduct(String query);
    Page<Product> getAllProduct(String category,
                                String brand,
                                String color,
                                String size,
                                Integer minPrice,
                                Integer maxPrice,
                                String minDiscount,
                                String stock,
                                String sort,
                                Integer pageNumber);

    List<Product> getProductsBySellerId(Long sellerId);
}
