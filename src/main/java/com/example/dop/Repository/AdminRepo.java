package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Admin;



public interface AdminRepo extends JpaRepository<Admin, Long>{

	List<Admin> findByAfirstnameContainingIgnoreCaseOrAemailContainingIgnoreCase(String keyword, String keyword2);

	List<Admin> findByAfirstnameContainingIgnoreCaseOrAemailContainingIgnoreCaseAndStatus(String keyword,
			String keyword2, String status);

	List<Admin> findByStatus(String statusFilter);

	List<Admin> findByAfirstnameContainingIgnoreCase(String keyword);

}

