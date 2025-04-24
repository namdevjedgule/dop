package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dop.Exception.ResourceNotFoundException;
import com.example.dop.Model.ActivityMaster;
import com.example.dop.Model.Company;
import com.example.dop.Repository.CompanyRepo;

@Service
public class CompanyService {

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Company saveCompany(Company company) {
		return companyRepo.save(company);
	}

	public List<Company> getAllCompanies() {
		return companyRepo.findAll();
	}

	public List<Company> getCompaniesCreatedByUser(String createdByEmail) {
		return companyRepo.findByCreatedBy(createdByEmail);
	}

	public List<Company> getCompaniesByActivity(ActivityMaster activity) {
		return companyRepo.findByActivity(activity);
	}

	public List<Company> getcompanyByStatus(String statusFilter) {
		return companyRepo.findByStatus(statusFilter);
	}

	public void deletecompanies(List<Long> selectedIds) {

		companyRepo.deleteAllById(selectedIds);
	}

	public Company updateCompany(Long id, Company updatedCompany, String updatedBy) {
		Company existingCompany = companyRepo.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));

		existingCompany.setCompanyName(updatedCompany.getCompanyName());
		existingCompany.setCemail(updatedCompany.getCemail());

		if (updatedCompany.getCpassword() != null && !updatedCompany.getCpassword().isBlank()) {
			existingCompany.setCpassword(passwordEncoder.encode(updatedCompany.getCpassword()));
		}

		if (updatedCompany.getActivity() != null) {
			existingCompany.setActivity(updatedCompany.getActivity());
		}

		existingCompany.setUpdatedBy(updatedBy);
		existingCompany.setUpdatedOn(LocalDateTime.now());

		return companyRepo.save(existingCompany);
	}

	public Company editcompany(Long cid) {
		return companyRepo.findById(cid).orElse(null);
	}

	public Company getCompanyById(Long id) {
		return companyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company not found"));
	}

	public void deleteCompany(Long id) {
		Optional<Company> companyOpt = companyRepo.findById(id);
		if (companyOpt.isPresent()) {
			companyRepo.deleteById(id);
		} else {
			throw new RuntimeException("Company not found with ID: " + id);
		}
	}

	public Company toggleCompanyStatus(Long id) {
		Optional<Company> companyOpt = companyRepo.findById(id);
		if (companyOpt.isPresent()) {
			Company company = companyOpt.get();
			company.setStatus(company.getStatus().equals("Active") ? "Inactive" : "Active");
			return companyRepo.save(company);
		}

		return null;
	}

	public Optional<Company> findByName(String companyName) {
		return companyRepo.findByCompanyNameIgnoreCase(companyName);
	}

}