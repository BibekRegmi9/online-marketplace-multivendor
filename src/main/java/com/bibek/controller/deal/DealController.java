package com.bibek.controller.deal;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Deal;
import com.bibek.model.HomeCategory;
import com.bibek.model.HomePage;
import com.bibek.service.deal.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-deal")
public class DealController extends BaseController {
    private final DealService dealService;
    private final CustomMessageSource customMessageSource;

    public DealController(DealService dealService, CustomMessageSource customMessageSource) {
        this.dealService = dealService;
        this.customMessageSource = customMessageSource;
    }

    @PostMapping("/create-deals")
    public ResponseEntity<GlobalApiResponse> create(@RequestBody Deal deal) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.DEAL)), dealService.createDeal(deal)));
    }

    @PatchMapping
    public ResponseEntity<GlobalApiResponse> update(@RequestBody Deal deal) {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_UPDATE, customMessageSource.get(MessageConstants.DEAL)), dealService.updateDeal(deal)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> update(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.DEAL)), null));
    }
}
