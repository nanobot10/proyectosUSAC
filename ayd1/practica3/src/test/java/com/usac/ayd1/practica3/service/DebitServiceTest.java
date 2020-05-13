package com.usac.ayd1.practica3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.usac.ayd1.practica3.entity.Account;
import com.usac.ayd1.practica3.entity.User;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.DebitRequest;
import com.usac.ayd1.practica3.payload.DebitResponse;
import com.usac.ayd1.practica3.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DebitServiceTest {

	@InjectMocks
	private DebitService debitService;

	@Mock
	private UserRepository userRepository;
	@Mock
	private TransactionService transactionService;

	@Test
	public void makeDebit() {

		DebitRequest request = createMockRequest();
		User user = CreateMockUser();
		when(userRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(user));
		ApiResponse response = debitService.makeDebit(request);

		assertEquals(Boolean.TRUE, response.getSuccess());
		assertEquals("success", response.getMessage());
		assertEquals(Double.valueOf(500.00), ((DebitResponse) response.getData()).getNewBalance());

	}

	@Test
	public void makeDebitUserNotFound() {
		DebitRequest request = createMockRequest();
		when(userRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.empty());
		ApiResponse response = debitService.makeDebit(request);

		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("User with provided account number does not exist", response.getMessage());
	}

	@Test
	public void makeDebitInsuficientFunds() {
		DebitRequest request = createMockRequest();
		request.setAmount(Double.valueOf(1500.00));
		User user = CreateMockUser();
		when(userRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(user));
		ApiResponse response = debitService.makeDebit(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("User has no funds in his account", response.getMessage());
	}

	@Test
	public void makeDebitInvalidRequestAmount() {
		DebitRequest request = createMockRequest();
		request.setAmount(Double.valueOf(-500.00));
		User user = CreateMockUser();
		when(userRepository.findByAccountNumber(request.getAccountNumber())).thenReturn(Optional.of(user));
		ApiResponse response = debitService.makeDebit(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("The amount must be greater than zero", response.getMessage());
	}

	private User CreateMockUser() {
		User user = new User();
		user.setName("User Test");
		user.setEmail("usertest1@example.com");
		user.setUsername("usertest1");
		user.setUserCode(Integer.valueOf(9999));
		Account account = new Account();
		account.setBalance(Double.valueOf(1000.00));
		account.setAccountNumber(UUID.randomUUID().toString());
		account.setUser(user);
		user.setAccount(account);
		return user;
	}

	private DebitRequest createMockRequest() {
		DebitRequest request = new DebitRequest();
		request.setAccountNumber(UUID.randomUUID().toString());
		request.setAmount(Double.valueOf(500.0));
		request.setDescription("test debit");
		return request;
	}

}
