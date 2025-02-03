package com.bibek.service.payment;

import com.bibek.model.Order;
import com.bibek.model.PaymentOrder;
import com.bibek.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId);
    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
    void createPayment(String paymentId, String paymentLinkId, String jwt) throws RazorpayException;

}
