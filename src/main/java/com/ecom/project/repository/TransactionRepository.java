package com.ecom.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	List<Transaction> findBySellerId(Long sellerId);
}
