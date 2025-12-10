package com.ecom.project.controller;

import com.ecom.project.domain.PaymentMethod;
import com.ecom.project.entity.*;
import com.ecom.project.repository.OrderItemRepository;
import com.ecom.project.repository.PaymentOrderRepository;
import com.ecom.project.response.PaymentLinkResponse;
import com.ecom.project.service.*;
import com.razorpay.PaymentLink;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final SellerService sellerService;

   

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(@RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader ("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        Set<Order> orders=orderService.createOrder(user,shippingAddress,cart);
        PaymentOrder paymentOrder=paymentService.createOrder(user,orders);
        PaymentLinkResponse response=new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)) {
        	PaymentLink payment=paymentService.createRazorPayPaymentLink(user, paymentOrder.getAmount(),
        																paymentOrder.getId());
        	String paymentUrl=payment.get("short_url");
        	 String paymentUrlId=payment.get("id");
        	 response.setPayment_link_url(paymentUrl);
        	 
        	 paymentOrder.setPaymentLinkId(paymentUrlId);
        	 paymentOrderRepository.save(paymentOrder);

        }else {
        	String paymentUrl=paymentService.createStripePaymentLink(paymentOrder.getAmount(),user, paymentOrder.getId());
        	response.setPayment_link_url(paymentUrl);
        	
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> updateOrderHistoryHandler(@RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        List<Order>orders=orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderItem>getOrderById(@PathVariable long orderId,
                                                 @RequestHeader("Authorization")String jwt
        )throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        OrderItem orderItem=orderService.findOrderItemById(orderId);
        return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order>cancelOrder(@PathVariable long orderId,
                                            @RequestHeader("Authorization")String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Order order=orderService.cancelOrder(orderId,user);

        Seller seller=sellerService.getSellerById(order.getSellerId());
        SellerReport sellerReport=sellerReportService.getSellerReport(seller);

        sellerReport.setCanceledOrders(sellerReport.getCanceledOrders()+1);
        sellerReport.setTotalRefunds(sellerReport.getTotalRefunds()+order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(sellerReport);


        return ResponseEntity.ok(order);
    }
}
