package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUserId(Long userId);
}
