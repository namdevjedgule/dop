package com.example.dop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {

	List<Admin> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String email);

	List<Admin> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndStatus(String firstName, String email,
			String status);

	List<Admin> findByStatus(String statusFilter);

	List<Admin> findByFirstNameContainingIgnoreCase(String keyword);

	Optional<Admin> findByEmail(String email);

}
