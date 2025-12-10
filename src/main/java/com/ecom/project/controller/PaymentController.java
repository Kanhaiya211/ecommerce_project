package com.ecom.project.controller;

import java.net.ResponseCache;
import java.security.PrivateKey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.project.entity.Order;
import com.ecom.project.entity.PaymentOrder;
import com.ecom.project.entity.Seller;
import com.ecom.project.entity.SellerReport;
import com.ecom.project.entity.User;
import com.ecom.project.response.ApiResponse;
import com.ecom.project.response.PaymentLinkResponse;
import com.ecom.project.service.PaymentService;
import com.ecom.project.service.SellerReportService;
import com.ecom.project.service.SellerService;
import com.ecom.project.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	
	@GetMapping("/api/payment/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,
			@RequestParam String paymentLinkId,
			@RequestHeader ("Authorization")String jwt)throws Exception{
		
		
		User user=userService.findUserByJwtToken(jwt);
		PaymentLinkResponse paymentLinkResponse;
		PaymentOrder paymentOrder=paymentService.getPaymentOrderByPaymentId(paymentLinkId);
		
		boolean  paymentSuccess=paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);
		if (paymentSuccess) {
			for(Order order:paymentOrder.getOrders()) {
				// transactionService.createTransaction(order);
				Seller seller=sellerService.getSellerById(order.getSellerId());
				SellerReport sellerReport=sellerReportService.getSellerReport(seller);
				sellerReport.setTotalOrders(sellerReport.getTotalOrders()+1);
				sellerReport.setTotalEarnings(sellerReport.getTotalEarnings()+order.getTotalSellingPrice());
				sellerReport.setTotalSales(sellerReport.getTotalSales()+order.getOrderItems().size());
				sellerReportService.updateSellerReport(sellerReport);
				
			}
			
		}
		ApiResponse response=new ApiResponse();
		response.setMessage("payment Succesful");
		response.setStatus(true);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
}
