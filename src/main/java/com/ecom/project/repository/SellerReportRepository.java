package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport,Long> {
	SellerReport findBySellerId(Long sellerId);
}
