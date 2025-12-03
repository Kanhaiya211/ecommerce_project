package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
