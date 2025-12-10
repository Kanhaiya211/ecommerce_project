package com.ecom.project.service;

import com.ecom.project.entity.Order;
import com.ecom.project.entity.PaymentOrder;
import com.ecom.project.entity.User;
import com.razorpay.PaymentLink;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders)throws Exception;
    PaymentOrder getPaymentOrderById(Long orderId)throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId)throws Exception;
    Boolean proceedPayment(PaymentOrder  paymentOrder,String paymentId,String paymentLinkId)throws Exception;

    PaymentLink createRazorPayPaymentLink(User user,Long amount,Long orderId)throws Exception;
    String createStripePaymentLink(Long amount,User user,Long orderId)throws Exception;
}
