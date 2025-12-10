package com.ecom.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.project.entity.Seller;
import com.ecom.project.entity.Transaction;
import com.ecom.project.service.SellerService;
import com.ecom.project.service.TransactionService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

	private final TransactionService transactionService;
	
	private final SellerService sellerService;
	@GetMapping()
	public ResponseEntity<List<Transaction>>getTransactionBySellerId(@RequestHeader ("Authorization")String jwt)throws Exception{
		Seller seller=sellerService.getSellerProfile(jwt);
		List<Transaction> transactions=transactionService.getTransactionsBySellerId(seller);
		
		return ResponseEntity.ok(transactions);
		
	}
	@GetMapping()
	public ResponseEntity<List<Transaction>>getAllTransations(@RequestHeader ("Authorization")String jwt)throws Exception{
		Seller seller=sellerService.getSellerProfile(jwt);
		List<Transaction> transactions=transactionService.getAllTransactions();
		return ResponseEntity.ok(transactions);
	}
	
	
}
