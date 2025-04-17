package com.example.dop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.ActivityMaster;
import com.example.dop.Model.Company;

public interface CompanyRepo extends JpaRepository<Company, Long> {

	List<Company> findByStatus(String statusFilter);

	Optional<Company> findByCompanyNameIgnoreCase(String companyName);

	List<Company> findByActivity(ActivityMaster activity);

	List<Company> findByCreatedBy(String createdBy);

}