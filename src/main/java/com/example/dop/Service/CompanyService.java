package com.example.dop.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.dop.Model.Company;
import com.example.dop.Repository.CompanyRepo;

@Service
public class CompanyService {

	@Autowired
	CompanyRepo companyRepo;

	public void saveCompany(Company c1)
	{
		
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

	
	
	public void deleteCompany(Long id) {
        if (companyRepo.existsById(id)) {
        	companyRepo.deleteById(id);
        } else {
            throw new RuntimeException("Subscription not found!");
        }
    }
	public Page<Company> getPaginatedCompanies(int page, int pageSize, String keyword, String statusFilter) {
		Pageable pageable = PageRequest.of(page, pageSize);

        return companyRepo.findAll(pageable);
	}
	 public void deleteCompanyById(Long id) {
	        companyRepo.deleteById(id);
	    }

	   
	  public void toggleCompanyStatus(Long id) {
	        Optional<Company> companyOpt = companyRepo.findById(id);
	        if (companyOpt.isPresent()) {
	            Company company = companyOpt.get();
	           
	            company.setStatus(company.getStatus().equals("Active") ? "Inactive" : "Active");
	            companyRepo.save(company);
	        }
	    }

}