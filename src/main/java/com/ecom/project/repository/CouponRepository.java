package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
	Coupon findByCode(String couponCode);
}
