package com.ecom.project.service.impl;

import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ecom.project.domain.PaymentOrderStatus;
import com.ecom.project.domain.PaymentStatus;
import com.ecom.project.entity.Order;
import com.ecom.project.entity.PaymentOrder;
import com.ecom.project.entity.User;
import com.ecom.project.repository.OrderRepository;
import com.ecom.project.repository.PaymentOrderRepository;
import com.ecom.project.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
	private final PaymentOrderRepository paymentOrderRepository;
	private final OrderRepository orderRepository;
	
	private String apikey="apiKey";
	private String apiSecret="apiSecret";
	@Override
	public PaymentOrder createOrder(User user, Set<Order> orders) throws Exception {
	
		Long amount=orders.stream().mapToLong(Order::getTotalSellingPrice).sum();
		PaymentOrder paymentOrder=new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setUser(user);
		paymentOrder.setOrders(orders);
		return paymentOrderRepository.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
		
		return paymentOrderRepository.findById(orderId).orElseThrow(()->new Exception("payment order not found"));
	}

	@Override
	public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
		PaymentOrder paymentOrder=paymentOrderRepository.findByPaymentLinkId(orderId);
		if (paymentOrder==null) {
			throw new Exception("payment order not found with provided payment link");
		}
		return paymentOrder;
	}

	@Override
	public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws Exception {
		if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			RazorpayClient razorpay=new RazorpayClient(apikey,apiSecret);
			Payment payment=razorpay.payments.fetch(paymentId);
			String status=payment.get("status");
			if (status.equals("captured")) {
			Set<Order>orders=paymentOrder.getOrders();
			for( Order order:orders) {
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
	public PaymentLink createRazorPayPaymentLink(User user, Long amount, Long orderId) throws Exception {
		
		amount=amount*100;
		try {
			RazorpayClient razorpayClient=new RazorpayClient(apikey,apiSecret);
			JSONObject paymentLinkRequest=new JSONObject();
			paymentLinkRequest.put("amount", amount);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customer=new JSONObject();
			customer.put("name", user.getFullName());
			customer.put("email", user.getEmail());
			paymentLinkRequest.put("customer", customer);
			
			JSONObject notify=new JSONObject();
			notify.put("email", true);
			
			paymentLinkRequest.put("callBackurl", "http://localhost:3000/payment-success/"+orderId);
			
			paymentLinkRequest.put("callback-method", "get");
			
			PaymentLink paymentLink=razorpayClient.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkUrl=paymentLink.get("short_url");
			String paymetnLinkId=paymentLink.get("id");
			
			return paymentLink;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
		
	}

	@Override
	public String createStripePaymentLink(Long amount, User user, Long orderId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
