package com.bibek.service.payment.impl;

import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.PaymentOrderStatus;
import com.bibek.enums.PaymentStatus;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.*;
import com.bibek.repository.OrderRepository;
import com.bibek.repository.PaymentOrderRepository;
import com.bibek.response.PaymentLinkResponse;
import com.bibek.service.payment.PaymentService;
import com.bibek.service.seller.SellerService;
import com.bibek.service.sellerReport.SellerReportService;
import com.bibek.service.transaction.TransactionService;
import com.bibek.service.user.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private final CustomMessageSource customMessageSource;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;
    private String apiKey = "apiKey";
    private String apiSecret = "apiSecret";
    private String stripeSecretKey="scripeSecretKey";


    public PaymentServiceImpl(PaymentOrderRepository paymentOrderRepository, OrderRepository orderRepository, CustomMessageSource customMessageSource, UserService userService, SellerService sellerService, SellerReportService sellerReportService, TransactionService transactionService) {
        this.paymentOrderRepository = paymentOrderRepository;
        this.orderRepository = orderRepository;
        this.customMessageSource = customMessageSource;
        this.userService = userService;
        this.sellerService = sellerService;
        this.sellerReportService = sellerReportService;
        this.transactionService = transactionService;
    }

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setOrders(orders);
        paymentOrder.setUser(user);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) {
        return paymentOrderRepository.findById(orderId).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.NOT_EXIST, customMessageSource.get(MessageConstants.PAYMENT_ORDER))));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(orderId);
        if(paymentOrder == null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.CRUD_NOT_EXIST, customMessageSource.get(MessageConstants.PAYMENT_ORDER)));
        }
        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            Payment payment = razorpayClient.payments.fetch(paymentId);

            String status = payment.get("status");
            if(status.equals("captured")){
                Set<Order> orders = paymentOrder.getOrders();
                for(Order order: orders){
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);
            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

//            String paymentLinkUrl = paymentLink.get("short_url");
//            String paymentLinkId = paymentLink.get("id");

            return paymentLink;
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Mero Pasal")
                                        .build())
                                .build())
                        .build())
                .build();
        Session session = Session.create(sessionCreateParams);
        return session.getUrl();
    }

    @Override
    public void createPayment(String paymentId, String paymentLinkId, String jwt) throws RazorpayException {
//        User user = userService.findUserByJwtToken(jwt);
//        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder = this.getPaymentOrderByPaymentId(paymentLinkId);

        Boolean paymentSuccess = this.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if(paymentSuccess){
            for(Order order: paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport sellerReport = sellerReportService.getSellerReport(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(sellerReport);
            }
        }
    }
}
