package com.bibek.controller.payment;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.service.payment.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController {
    private final PaymentService paymentService;
    private final CustomMessageSource customMessageSource;

    public PaymentController(PaymentService paymentService, CustomMessageSource customMessageSource) {
        this.paymentService = paymentService;
        this.customMessageSource = customMessageSource;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<GlobalApiResponse> createPayment(@PathVariable String paymentId,
                                                           @RequestParam String paymentLinkId,
                                                           @RequestHeader("Authorization") String jwt) throws RazorpayException {
        paymentService.createPayment(paymentId, paymentLinkId, jwt);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.PAYMENT)), null));
    }

}
