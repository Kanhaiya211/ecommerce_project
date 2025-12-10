package com.ecom.project.service;

import java.util.List;

import com.ecom.project.entity.Order;
import com.ecom.project.entity.Seller;
import com.ecom.project.entity.Transaction;

public interface TransactionService {
	
Transaction createTransaction(Order order)throws Exception;
List<Transaction>getTransactionsBySellerId(Seller seller)throws Exception;
List<Transaction>getAllTransactions()throws Exception;
}
