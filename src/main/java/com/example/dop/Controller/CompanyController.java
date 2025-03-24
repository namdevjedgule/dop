package com.example.dop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.dop.Model.Company;
import com.example.dop.Service.CompanyService;

@Controller
public class CompanyController 
{
	@Autowired
	CompanyService companyService;
	
	@GetMapping("/client/add")
	public String add()
	{
		return "companyAdd";
	}

		@PostMapping("/saveCompany")
	public String save(@ModelAttribute("c1")Company c1)
	{
		c1.setStatus("Active");
		companyService.saveCompany(c1);
		return "redirect:/client/add";
	}
	@PostMapping("/deleteSelected1")
    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
                                 RedirectAttributes redirectAttributes) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
        	companyService.deletecompanies(selectedIds);
            redirectAttributes.addFlashAttribute("message", "Selected admins deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No admins selected for deletion.");
        }
        return "redirect:/client/list";
    }
    
    @GetMapping("/editcompany/{id}")
    public String EditCompany(@PathVariable("id") Long id, Model model) {
        Company c1 = companyService.editcompany(id);
        
        if (c1 == null) { 
            return "redirect:/companyList?error=CompanyNotFound";
        }

        model.addAttribute("ap", c1);
        return "EditCompany"; 
    }


    @PostMapping("/UpdateCompany")
    public String updateCompany(@ModelAttribute("ap") Company ap) {
        if (ap.getCid() == null) {
            return "redirect:/client/list?error=InvalidCompanyId";
        }

        Company existingCompany = companyService.getCompanyById(ap.getCid());

        if (existingCompany != null) { 
            
            String existingStatus = existingCompany.getStatus(); 
            
           
            existingCompany.setCompanyName(ap.getCompanyName());
            existingCompany.setActivity(ap.getActivity());
            existingCompany.setCemail(ap.getCemail());

           
            if (ap.getCpassword() != null && !ap.getCpassword().trim().isEmpty()) {
                existingCompany.setCpassword(ap.getCpassword());
            }

            existingCompany.setStatus(existingStatus);

            companyService.saveCompany(existingCompany);
        }

        return "redirect:/client/list?success=CompanyUpdated";
    }

    @GetMapping("/deletecompany/{id}")
    public String delCompany(@PathVariable("id") Long id)
    {
    	companyService.deleteCompany(id);
    	return"redirect:/client/list";
    }
    
    @GetMapping("/client/list")
    public String listCompany(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "statusFilter", required = false) String statusFilter,
            Model model) {

        List<Company> company;

        if (keyword != null) keyword = keyword.trim();

        if (keyword != null && !keyword.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
        	company = companyService.searchcompanyByStatus(keyword, statusFilter);
        } else if (keyword != null && !keyword.isEmpty()) {
        	company = companyService.searchCompanies(keyword);
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
           
        	company = companyService.getcompanyByStatus(statusFilter);
        } else {
        	company = companyService.getCompanies();
        }

        model.addAttribute("company", company);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", statusFilter); 

        return "companyList"; 
    }
	
}