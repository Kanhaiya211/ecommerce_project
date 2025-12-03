package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
	Wishlist findByUserId(Long userId);
}
