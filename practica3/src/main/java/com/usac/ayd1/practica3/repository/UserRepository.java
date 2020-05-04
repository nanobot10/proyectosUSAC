package com.usac.ayd1.practica3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usac.ayd1.practica3.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);

	List<User> findByIdIn(List<Long> userIds);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	@Query("select max(u.userCode) from User u")
	Integer getMaxUserCode();

	@Query("select u from User u where u.account.accountNumber = ?1")
	Optional<User> findByAccountNumber(String accountNumber);
	
	

}
