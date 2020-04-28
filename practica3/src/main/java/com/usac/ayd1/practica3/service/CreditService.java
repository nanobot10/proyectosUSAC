package com.usac.ayd1.practica3.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usac.ayd1.practica3.entity.Credit;
import com.usac.ayd1.practica3.enums.Status;
import com.usac.ayd1.practica3.enums.TransactionType;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.CreditResponse;
import com.usac.ayd1.practica3.repository.CreditRepository;
import com.usac.ayd1.practica3.repository.UserRepository;

@Service
public class CreditService {

	@Autowired
	private CreditRepository creditRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionService transactionService;

	public ApiResponse getAllCredits() {
		return new ApiResponse(true, "success", creditRepository.findAll().stream()
				.map(credit -> new CreditResponse(credit.getId(), credit.getStatus(),
						credit.getUser().getAccount().getAccountNumber(), credit.getCreatedAt(), credit.getAmount()))
				.collect(Collectors.toList()));
	}

	public ApiResponse getCredit(Long id) {
		Optional<Credit> credit = creditRepository.findById(id);

		if (!credit.isPresent()) {
			return new ApiResponse(false, "credit not found");
		}

		return new ApiResponse(true, "success",
				new CreditResponse(id, credit.get().getStatus(), credit.get().getUser().getAccount().getAccountNumber(),
						credit.get().getCreatedAt(), credit.get().getAmount()));
	}

	public ApiResponse changeCreditStatus(Long id, Status status) {

		Optional<Credit> credit = creditRepository.findById(id);

		if (!credit.isPresent()) {
			return new ApiResponse(false, "credit not found");
		}

		if (!Status.PENDING.equals(credit.get().getStatus())) {
			return new ApiResponse(false, "The credit has already been changed status");
		}

		if (Status.APPROVED.equals(status)) {
			credit.get().getUser().getAccount()
					.setBalance(credit.get().getUser().getAccount().getBalance() + credit.get().getAmount());
			userRepository.save(credit.get().getUser());
			transactionService.saveTransaction(credit.get().getUser(), "Bank Credit id: " + credit.get().getId(),
					TransactionType.CREDIT, credit.get().getAmount(), "approved credit");
		}

		credit.get().setStatus(status);
		creditRepository.save(credit.get());

		return new ApiResponse(true, "success",
				new CreditResponse(id, status, credit.get().getUser().getAccount().getAccountNumber(),
						credit.get().getCreatedAt(), credit.get().getAmount()));

	}

}
