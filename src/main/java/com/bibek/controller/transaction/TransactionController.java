package com.bibek.controller.transaction;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Seller;
import com.bibek.service.payment.PaymentService;
import com.bibek.service.seller.SellerService;
import com.bibek.service.transaction.TransactionService;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController extends BaseController {
    private final CustomMessageSource customMessageSource;
    private final PaymentService paymentService;
    private final SellerService sellerService;
    private final TransactionService transactionService;

    public TransactionController(CustomMessageSource customMessageSource, PaymentService paymentService, SellerService sellerService, TransactionService transactionService) {
        this.customMessageSource = customMessageSource;
        this.paymentService = paymentService;
        this.sellerService = sellerService;
        this.transactionService = transactionService;
    }

    @GetMapping("/seller")
    public ResponseEntity<GlobalApiResponse> createPayment(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PAYMENT)), transactionService.getTransactionsBySellerId(seller)));
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getAllTransaction() {
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PAYMENT)), transactionService.getAllTransactions()));
    }
}
