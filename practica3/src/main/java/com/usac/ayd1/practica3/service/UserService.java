package com.usac.ayd1.practica3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.usac.ayd1.practica3.entity.Account;
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
import com.usac.ayd1.practica3.repository.AccountRepository;
import com.usac.ayd1.practica3.repository.CreditRepository;
import com.usac.ayd1.practica3.repository.UserRepository;
import com.usac.ayd1.practica3.security.UserPrincipal;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
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
				new UserProfileResponse(user.getName(), user.getEmail(), user.getUsername(),
						user.getAccount() != null ? user.getAccount().getAccountNumber() : "", user.getUserCode(),
						new ArrayList<>(user.getRoles())));

	}

	public ApiResponse delete(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			return new ApiResponse(false, "User not found");
		}
		userRepository.delete(user.get());
		return new ApiResponse(true, "success");
	}

	public ApiResponse getUserBalance() {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return new ApiResponse(false, "User is not authenticated");
		}

		User user = getUserAuthenticated();

		if (user.getAccount() != null) {
			return new ApiResponse(true, "success", new UserBalance(user.getName(),
					user.getAccount().getAccountNumber(), user.getAccount().getBalance()));
		} else {
			return new ApiResponse(true, "success", new UserBalance(user.getName(), "no account", Double.valueOf(0.0)));
		}

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

		User userTo = getUserAuthenticated();

		Credit credit = new Credit();
		credit.setAmount(creditRequest.getAmount());
		credit.setDescription(creditRequest.getDescription());
		credit.setStatus(Status.PENDING);
		credit.setUser(userTo);

		creditRepository.saveAndFlush(credit);

		return new ApiResponse(true, "success",
				new CreditResponse(credit.getId(), credit.getStatus(), credit.getDescription(),
						userTo.getAccount().getAccountNumber(), credit.getCreatedAt(), credit.getAmount()));

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

	public ApiResponse getAllUsers(String accountNumber) {

		List<Account> accounts = null;
		if (StringUtils.isEmpty(accountNumber)) {
			accounts = accountRepository.findAll();
		} else {
			accounts = accountRepository.findByAccountNumberContaining(accountNumber);
		}
		List<UserSummaryResponse> users = new ArrayList<>();
		for (Account acc : accounts) {
			users.add(createUserSummary(acc.getUser()));
		}
		return new ApiResponse(true, "success", users);
	}

	public ApiResponse getUserSummary() {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return new ApiResponse(false, "User is not authenticated");
		}
		User user = getUserAuthenticated();

		UserSummaryResponse userSummary = createUserSummary(user);

		return new ApiResponse(true, "success", userSummary);
	}

	private UserSummaryResponse createUserSummary(User user) {
		return new UserSummaryResponse(user.getId(), user.getName(), user.getUsername(), user.getUserCode(),
				user.getEmail(), (user.getAccount() != null ? user.getAccount().getAccountNumber() : "no account"),
				(user.getAccount() != null ? user.getAccount().getBalance() : Double.valueOf(0.0)), user.getCreatedAt(),
				mapCredits(user.getCredits()), mapTransactions(user.getTransactions()),
				new ArrayList<>(user.getRoles()));
	}

	private List<CreditResponse> mapCredits(List<Credit> credits) {
		return credits.stream()
				.map(credit -> new CreditResponse(credit.getId(), credit.getStatus(), credit.getDescription(),
						credit.getUser().getAccount().getAccountNumber(), credit.getCreatedAt(), credit.getAmount()))
				.collect(Collectors.toList());
	}

	private List<TransactionResponse> mapTransactions(List<Transaction> transactions) {
		return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
	}

}
