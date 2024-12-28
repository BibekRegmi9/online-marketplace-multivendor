package com.bibek.model;

import com.bibek.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    private String paymentId;

    private String esewaPaymentLinkId;

    private String esewaPaymentLinkReferenceId;

    private String esewaPaymentLinkStatus;

    private String esewaPaymentIdZWSP;

    private PaymentStatus status;
}
