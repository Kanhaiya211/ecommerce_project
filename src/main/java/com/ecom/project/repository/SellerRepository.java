package com.ecom.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.domain.AccountStatus;
import com.ecom.project.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller,Long> {
	 Seller findByEmail(String email);
	    List<Seller> findByAccountStatus(AccountStatus status);
}
