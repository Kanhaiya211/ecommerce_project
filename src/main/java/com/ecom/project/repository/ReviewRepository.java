package com.ecom.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Review;

public interface ReviewRepository extends JpaRepository<Review,Long>{
	  List<Review> findReviewsByUserId(Long userId);
	    List<Review> findReviewsByProductId(Long productId);
}
