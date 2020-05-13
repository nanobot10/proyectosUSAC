package com.usac.ayd1.practica3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usac.ayd1.practica3.entity.Transaction;
import com.usac.ayd1.practica3.entity.User;
import com.usac.ayd1.practica3.enums.TransactionType;
import com.usac.ayd1.practica3.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public void saveTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

	public Transaction createTransaction(User user, String accountNumber, TransactionType transactionType,
			Double amount, String description) {
		Transaction transaction = new Transaction();
		transaction.setAccountNumber(accountNumber);
		transaction.setTransactionType(transactionType);
		transaction.setAmount(amount);
		transaction.setUser(user);
		transaction.setDescription(description);
		return transaction;
	}

}
