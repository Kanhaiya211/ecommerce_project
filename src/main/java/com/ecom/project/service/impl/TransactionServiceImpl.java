package com.ecom.project.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.project.entity.Order;
import com.ecom.project.entity.Seller;
import com.ecom.project.entity.Transaction;
import com.ecom.project.repository.SellerRepository;
import com.ecom.project.repository.TransactionRepository;
import com.ecom.project.service.TransactionService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
	
	private final TransactionRepository transactionRepository;
	private final SellerRepository sellerRepository;
	@Override
	public Transaction createTransaction(Order order) throws Exception {
		Seller seller=sellerRepository.findById(order.getSellerId()).get();
		Transaction transaction=new Transaction();
		transaction.setSeller(seller);
		transaction.setCustomer(order.getUser());
		transaction.setOrder(order);
		
	
		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionsBySellerId(Seller seller) throws Exception {
		// TODO Auto-generated method stub
		return transactionRepository.findBySellerId(seller.getId());
	}

	@Override
	public List<Transaction> getAllTransactions() throws Exception {
		// TODO Auto-generated method stub
		return transactionRepository.findAll();
	}

}
