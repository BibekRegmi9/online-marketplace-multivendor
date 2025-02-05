package com.bibek.controller.homeCategory;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Coupon;
import com.bibek.model.HomeCategory;
import com.bibek.model.HomePage;
import com.bibek.service.homeCategory.HomeCategoryService;
import com.bibek.service.homeService.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home-category")
public class HomeCategoryController extends BaseController {
    private final HomeCategoryService homeCategoryService;
    private final CustomMessageSource customMessageSource;
    private final HomeService homeService;

    public HomeCategoryController(HomeCategoryService homeCategoryService, CustomMessageSource customMessageSource, HomeService homeService) {
        this.homeCategoryService = homeCategoryService;
        this.customMessageSource = customMessageSource;
        this.homeService = homeService;
    }

    @PostMapping("/create-categories")
    public ResponseEntity<GlobalApiResponse> create(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        HomePage home = homeService.createHomePageData(categories);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.HOME_CATEGORY)), home));
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getAll() {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.HOME_CATEGORY)), homeCategoryService.getAll()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> update(@RequestBody HomeCategory homeCategory, @PathVariable Long id) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.HOME_CATEGORY)), homeCategoryService.update(homeCategory, id)));
    }
}
