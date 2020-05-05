package com.usac.ayd1.practica3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usac.ayd1.practica3.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findAllByOrderByCreatedAtDesc();

}
