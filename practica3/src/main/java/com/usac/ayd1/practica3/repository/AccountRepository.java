package com.usac.ayd1.practica3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usac.ayd1.practica3.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByAccountNumberContaining(String accountNumber);

}
