package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Product;
import com.ecom.project.entity.Cart;
import com.ecom.project.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
