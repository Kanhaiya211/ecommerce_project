package com.ecom.project.service.impl;

import com.ecom.project.domain.OrderStatus;
import com.ecom.project.domain.PaymentStatus;
import com.ecom.project.entity.*;
import com.ecom.project.repository.AddressRepository;
import com.ecom.project.repository.OrderItemRepository;
import com.ecom.project.repository.OrderRepository;
import com.ecom.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    public Set<Order> createOrder(User user, Address shippingaddress, Cart cart) throws Exception {

    if (!user.getAddresses().contains(shippingaddress)){
        user.getAddresses().add(shippingaddress);
    }
    Address address=addressRepository.save(shippingaddress);
        Map<Long,List<CartItem>> itemsBySeller=cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item->item.getProduct()
                        .getSeller().getId()));
        Set<Order>orders=new HashSet<>();
        for (Map.Entry<Long,List<CartItem>> entry:itemsBySeller.entrySet()){
            Long id=entry.getKey();
            List<CartItem>items=entry.getValue();
            int total_order_price=items.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();
            int total_item=items.stream().mapToInt(
                    CartItem::getQuantity
            ).sum();
            Order createdOrder=new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(id);
            createdOrder.setTotalMrpPrice(total_order_price);
            createdOrder.setTotalItem(total_item);
            createdOrder.setTotalSellingPrice(total_order_price);
            createdOrder.setShippingAddress(shippingaddress);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder=orderRepository.save(createdOrder);
            orders.add(savedOrder);
            List<OrderItem>orderItems=new ArrayList<>();
            for (CartItem item:items){
                OrderItem orderItem=new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());
                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem=orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }
        return orders;
    }

    @Override
    public Order findOrderById(long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()-> new Exception("Order not found"));
    }

    @Override
    public List<Order> userOrderHistory(long userId) throws Exception {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(long sellerId) throws Exception {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(long orderId, OrderStatus orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(long orderId, User user) throws Exception {
        Order order=findOrderById(orderId);
        if (!user.getId().equals(order.getUser().getId())){
            throw new Exception("you don't have access");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->new ExpressionException("items not found"));
    }
}
