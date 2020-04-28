package com.usac.ayd1.practica3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.usac.ayd1.practica3.entity.Credit;
import com.usac.ayd1.practica3.entity.Transaction;
import com.usac.ayd1.practica3.entity.User;
import com.usac.ayd1.practica3.enums.Status;
import com.usac.ayd1.practica3.enums.TransactionType;
import com.usac.ayd1.practica3.exception.AppException;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.CreditRequest;
import com.usac.ayd1.practica3.payload.CreditResponse;
import com.usac.ayd1.practica3.payload.TransactionResponse;
import com.usac.ayd1.practica3.payload.TransferRequest;
import com.usac.ayd1.practica3.payload.TransferResponse;
import com.usac.ayd1.practica3.payload.UserBalance;
import com.usac.ayd1.practica3.payload.UserProfileResponse;
import com.usac.ayd1.practica3.payload.UserSummaryResponse;
import com.usac.ayd1.practica3.repository.CreditRepository;
import com.usac.ayd1.practica3.repository.UserRepository;
import com.usac.ayd1.practica3.security.UserPrincipal;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CreditRepository creditRepository;
	@Autowired
	private TransactionService transactionService;

	public ApiResponse getUserProfile() {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return new ApiResponse(false, "User is not authenticated");
		}
		User user = getUserAuthenticated();
		return new ApiResponse(true, "success",
				new UserProfileResponse(user.getUsername(),
						user.getAccount() != null ? user.getAccount().getAccountNumber() : "", user.getUserCode(),
						new ArrayList<>(user.getRoles())));

	}

	public ApiResponse getUserBalance() {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return new ApiResponse(false, "User is not authenticated");
		}

		User user = getUserAuthenticated();

		return new ApiResponse(true, "success",
				new UserBalance(user.getName(), user.getAccount().getAccountNumber(), user.getAccount().getBalance()));

	}

	public ApiResponse makeTransfer(TransferRequest transferRequest) {

		User userFrom = getUserAuthenticated();

		if (validateAmount(userFrom, transferRequest.getAmount())) {
			return new ApiResponse(false, "Inssuficient Funds");
		}

		Optional<User> userTo = userRepository.findByAccountNumber(transferRequest.getAccountNumber());

		if (!userTo.isPresent()) {
			return new ApiResponse(false, "User with provided account number does not exist");
		}

		userFrom.getAccount().setBalance(userFrom.getAccount().getBalance() - transferRequest.getAmount());
		userTo.get().getAccount().setBalance(userTo.get().getAccount().getBalance() + transferRequest.getAmount());
		userRepository.save(userFrom);
		userRepository.save(userTo.get());

		transactionService.saveTransaction(userFrom, userTo.get().getAccount().getAccountNumber(),
				TransactionType.DEBIT, transferRequest.getAmount(), "transfer between accounts");
		transactionService.saveTransaction(userTo.get(), userFrom.getAccount().getAccountNumber(),
				TransactionType.CREDIT, transferRequest.getAmount(), "transfer between accounts");

		return new ApiResponse(true, "success", new TransferResponse(userFrom.getAccount().getBalance()));
	}

	public ApiResponse applyForCredit(CreditRequest creditRequest) {

		Optional<User> userTo = userRepository.findByAccountNumber(creditRequest.getAccountNumber());

		if (!userTo.isPresent()) {
			return new ApiResponse(false, "User with provided account number does not exist");
		}

		Credit credit = new Credit();
		credit.setAmount(creditRequest.getAmount());
		credit.setDescription(creditRequest.getDescription());
		credit.setStatus(Status.PENDING);
		credit.setUser(userTo.get());

		creditRepository.saveAndFlush(credit);

		return new ApiResponse(true, "success", new CreditResponse(credit.getId(), credit.getStatus(),
				userTo.get().getAccount().getAccountNumber(), credit.getCreatedAt(), credit.getAmount()));

	}

	private boolean validateAmount(User userFrom, Double amount) {
		return userFrom.getAccount().getBalance() < amount;
	}

	private User getUserAuthenticated() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Optional<User> user = userRepository.findByUsername(userPrincipal.getUsername());

		if (!user.isPresent()) {
			throw new AppException("User authenticated not found");
		}

		return user.get();
	}

	public ApiResponse getUserSummary() {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return new ApiResponse(false, "User is not authenticated");
		}
		User user = getUserAuthenticated();

		UserSummaryResponse userSummary = new UserSummaryResponse(user.getId(), user.getUsername(),
				user.getAccount().getAccountNumber(), user.getUserCode(), mapCredits(user.getCredits()),
				mapTransactions(user.getTransactions()));

		return new ApiResponse(true, "success", userSummary);
	}

	private List<CreditResponse> mapCredits(List<Credit> credits) {
		return credits.stream()
				.map(credit -> new CreditResponse(credit.getId(), credit.getStatus(),
						credit.getUser().getAccount().getAccountNumber(), credit.getCreatedAt(), credit.getAmount()))
				.collect(Collectors.toList());
	}

	private List<TransactionResponse> mapTransactions(List<Transaction> transactions) {
		return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
	}

}
