package com.ecom.project.service;

import com.ecom.project.domain.OrderStatus;
import com.ecom.project.entity.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order>createOrder(User user , Address shippingaddress, Cart cart)throws Exception;
    Order findOrderById(long id)throws Exception;
    List<Order>userOrderHistory(long userId)throws Exception;
    List<Order>sellersOrder(long sellerId)throws Exception;
    Order updateOrderStatus(long orderId, OrderStatus orderStatus)throws Exception;
    Order cancelOrder(long orderId,User user)throws Exception;
    OrderItem findOrderItemById(long id)throws  Exception;
}
