package com.example.dop.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dop.Model.Company;
import com.example.dop.Repository.CompanyRepo;



@Service
public class CompanyService {

	@Autowired
	CompanyRepo companyRepo;

	public void saveCompany(Company c1)
	{
		c1.setCreated_on(LocalDate.now());
		companyRepo.save(c1);
		
	}
public List<Company> getCompanies() {
		
		return companyRepo.findAll();
	}

	public List<Company> searchCompanies(String keyword) {
        return companyRepo.findByCompanyNameContainingIgnoreCaseOrCemailContainingIgnoreCaseOrActivityContainingIgnoreCase(keyword, keyword, keyword);
    }

	public List<Company> searchcompanyByStatus(String keyword, String status) {
	    return companyRepo.findByCompanyNameContainingIgnoreCaseOrCemailContainingIgnoreCaseAndStatus(keyword, keyword, status);
	}


	public List<Company> getcompanyByStatus(String statusFilter) {
	    return companyRepo.findByStatus(statusFilter);
	}

	public void deletecompanies(List<Long> selectedIds) {
		
		companyRepo.deleteAllById(selectedIds);
	    }

	public Company editcompany(Long cid) {
	    return companyRepo.findById(cid).orElse(null); // Return null if not found
	}

	public Company getCompanyById(Long cid) {
	
		return companyRepo.getById(cid);
	}

	
	
	public void deleteCompany(Long cid) 
	{
		companyRepo.deleteById(cid);
		
	}
	
		

	
}