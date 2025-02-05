package com.bibek.controller.admin;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.enums.AccountStatus;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.service.seller.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private final SellerService sellerService;
    private final CustomMessageSource customMessageSource;

    public AdminController(SellerService sellerService, CustomMessageSource customMessageSource) {
        this.sellerService = sellerService;
        this.customMessageSource = customMessageSource;
    }

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<GlobalApiResponse> updateSellerStatus(@PathVariable Long id, @PathVariable AccountStatus status) {
        return ResponseEntity.ok(successResponse(customMessageSource.get("Seller Account Status updated."), sellerService.updateSellerAccountStatus(id, status)));
    }
}
