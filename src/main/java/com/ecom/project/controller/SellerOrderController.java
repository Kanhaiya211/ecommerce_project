package com.ecom.project.controller;

import com.ecom.project.domain.OrderStatus;
import com.ecom.project.entity.Order;
import com.ecom.project.entity.Seller;
import com.ecom.project.service.OrderService;
import com.ecom.project.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/seller/orders")
public class SellerOrderController {
    private final OrderService orderService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Order>>getAllOrdersHandler(@RequestHeader ("Authorization")String jwt)throws Exception{

        Seller seller=sellerService.getSellerProfile(jwt);
        List<Order>orders=orderService.sellersOrder(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@RequestHeader("Authorization")String jwt,
                                                   @PathVariable long orderId,
                                                   @PathVariable OrderStatus orderStatus)throws Exception{
        Order order=orderService.updateOrderStatus(orderId,orderStatus);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }
}
