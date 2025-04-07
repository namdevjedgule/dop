package com.example.dop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Company;
import com.example.dop.Model.User;
import com.example.dop.Service.CompanyService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CompanyController {
	@Autowired
	CompanyService companyService;

	@GetMapping("/client/add")
	public String add(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		return "companyAdd";
	}

	@PostMapping("/saveCompany")
	public String save(@ModelAttribute("c1") Company c1) {
		c1.setStatus("Active");
		companyService.saveCompany(c1);
		return "redirect:/client/add";
	}

//	@PostMapping("/deleteSelected1")
//    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
//                                 RedirectAttributes redirectAttributes) {
//        if (selectedIds != null && !selectedIds.isEmpty()) {
//        	companyService.deletecompanies(selectedIds);
//            redirectAttributes.addFlashAttribute("message", "Selected admins deleted successfully.");
//        } else {
//            redirectAttributes.addFlashAttribute("error", "No admins selected for deletion.");
//        }
//        
//        return "redirect:/client/list";
//    }
//    

	@GetMapping("/client/list")
	public String listCompany(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusFilter", required = false) String statusFilter, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<Company> company;

		if (keyword != null)
			keyword = keyword.trim();

		if (keyword != null && !keyword.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
			company = companyService.searchcompanyByStatus(keyword, statusFilter);
		} else if (keyword != null && !keyword.isEmpty()) {
			company = companyService.searchCompanies(keyword);
		} else if (statusFilter != null && !statusFilter.isEmpty()) {

			company = companyService.getcompanyByStatus(statusFilter);
		} else {
			company = companyService.getCompanies();
		}
		int pageSize = 5;

		Page<Company> companyPage = companyService.getPaginatedCompanies(page, pageSize, keyword, statusFilter);

		model.addAttribute("company", companyPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", companyPage.getTotalPages());

		model.addAttribute("company", company);
		model.addAttribute("keyword", keyword);
		model.addAttribute("statusFilter", statusFilter);

		return "companyList";
	}

	@GetMapping("/editcompany/{id}")
	public String EditCompany(@PathVariable("id") Long id, Model model, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", loggedInUser.getFirstName());
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

	@PostMapping("/deleteSelectedCompany")
	public String deleteSelectedCompany(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
			RedirectAttributes redirectAttributes) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			companyService.deletecompanies(selectedIds);
			redirectAttributes.addFlashAttribute("successMessage", "Selected companies deleted successfully.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "No companies selected for deletion.");
		}
		return "redirect:/client/list";
	}

	@GetMapping("/client/deleteCompany/{id}")
	public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			companyService.deleteCompanyById(id);
			redirectAttributes.addFlashAttribute("success", "Company deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error deleting company!");
		}
		return "redirect:/client/list";
	}

	@GetMapping("/toggleStatus/{id}")
	public String toggleCompanyStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		companyService.toggleCompanyStatus(id);
		redirectAttributes.addFlashAttribute("message", "Company status updated successfully!");
		return "redirect:/client/list";
	}
}
