package com.ecom.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.domain.PayoutsStatus;
import com.ecom.project.entity.Payouts;

public interface PayoutsRepository extends JpaRepository<Payouts,Long> {
	 List<Payouts> findPayoutsBySellerId(Long sellerId);
	    List<Payouts> findAllByStatus(PayoutsStatus status);
}
