package com.bibek.service.product.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Category;
import com.bibek.model.Product;
import com.bibek.model.Seller;
import com.bibek.repository.CategoryRepository;
import com.bibek.repository.ProductRepository;
import com.bibek.request.CreateProductRequest;
import com.bibek.service.product.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomMessageSource customMessageSource;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, CustomMessageSource customMessageSource) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public Product createProduct(CreateProductRequest productReq, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(productReq.getCategory());

        if(category1 == null){
            Category newCategory = new Category();
            newCategory.setCategoryId(productReq.getCategory());
            newCategory.setLevel(1);
            category1 = categoryRepository.save(newCategory);
        }

        Category category2 = categoryRepository.findByCategoryId(productReq.getCategory2());

        if(category2 == null){
            Category newCategory2 = new Category();
            newCategory2.setCategoryId(productReq.getCategory2());
            newCategory2.setLevel(2);
            newCategory2.setParentCategory(category1);
            category2 = categoryRepository.save(newCategory2);
        }

        Category category3 = categoryRepository.findByCategoryId(productReq.getCategory3());

        if(category3 == null){
            Category newCategory3 = new Category();
            newCategory3.setCategoryId(productReq.getCategory3());
            newCategory3.setLevel(3);
            newCategory3.setParentCategory(category2);
            category3 = categoryRepository.save(newCategory3);
        }

        Integer discountPercentage = calculateDiscountPercentage(productReq.getMrpPrice(),productReq.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(productReq.getDescription());
        product.setTitle(productReq.getTitle());
        product.setColor(productReq.getColor());
        product.setSellingPrice(productReq.getSellingPrice());
        product.setImages(productReq.getImages());
        product.setMrpPrice(productReq.getMrpPrice());
        product.setSizes(productReq.getSizes());
        product.setDiscountPercentage(discountPercentage);

        return productRepository.save(product);

    }

    private Integer calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <= 0){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.MRP_PRICE_ILLEGAL));
        }

        double discount = mrpPrice - sellingPrice;
        double discountPercent = (discount/mrpPrice) * 100;
        return (int) discountPercent;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existProduct = findByProductId(id);


        return null;
    }

    @Override
    public Product findByProductId(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.PRODUCT))));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProduct(String category, String brand, String color, String size, Integer minPrice, Integer maxPrice, Integer minDiscount, String stock, String sort, Integer pageNumber) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(category != null){
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            if(color != null && !color.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("color"), color));
            }

            if(size != null && !size.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("size"), size));
            }

            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }

            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            if(minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }

            if(stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if(sort != null && !sort.isEmpty()){
            pageable = switch (sort) {
                case "price_low" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" ->
                        PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
            };
        } else {
            pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }

        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
