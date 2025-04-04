package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Company;

public interface CompanyRepo extends JpaRepository<Company,Long>{

	
	List<Company> findByStatus(String statusFilter);

	List<Company> findByCompanyNameContainingIgnoreCaseOrCemailContainingIgnoreCase(String keyword, String keyword2);

	List<Company> findByCompanyNameContainingIgnoreCaseOrCemailContainingIgnoreCaseAndStatus(String keyword,
			String keyword2, String status);

	List<Company> findByCompanyNameContainingIgnoreCaseOrCemailContainingIgnoreCaseOrActivityContainingIgnoreCase(
			String keyword, String keyword2, String keyword3);

	Page<Company> findByCompanyNameContainingAndStatus(String keyword, String statusFilter, Pageable pageable);

	Page<Company> findByCompanyNameContaining(String keyword, Pageable pageable);

}
