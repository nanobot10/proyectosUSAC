package com.usac.ayd1.practica3.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usac.ayd1.practica3.entity.User;
import com.usac.ayd1.practica3.enums.TransactionType;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.DebitRequest;
import com.usac.ayd1.practica3.payload.DebitResponse;
import com.usac.ayd1.practica3.repository.UserRepository;

@Service
public class DebitService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionService transactionService;

	public ApiResponse makeDebit(DebitRequest debitRequest) {

		if (debitRequest.getAmount() <= 0) {
			return new ApiResponse(false, "The amount must be greater than zero");
		}

		Optional<User> user = userRepository.findByAccountNumber(debitRequest.getAccountNumber());

		if (!user.isPresent()) {
			return new ApiResponse(false, "User with provided account number does not exist");
		}

		if (user.get().getAccount().getBalance() < debitRequest.getAmount()) {
			return new ApiResponse(false, "User has no funds in his account");
		}

		user.get().getAccount().setBalance(user.get().getAccount().getBalance() - debitRequest.getAmount());
		userRepository.save(user.get());
		transactionService.saveTransaction(transactionService.createTransaction(user.get(), "Bank Debit",
				TransactionType.DEBIT, debitRequest.getAmount(), debitRequest.getDescription()));

		return new ApiResponse(true, "success", new DebitResponse(user.get().getAccount().getBalance()));

	}

}
