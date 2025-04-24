package com.example.dop.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dop.Model.ActivityMaster;
import com.example.dop.Model.Company;
import com.example.dop.Model.User;
import com.example.dop.Repository.ActivityMasterRepository;
import com.example.dop.Repository.CompanyRepo;
import com.example.dop.Service.CompanyService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/companies")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private ActivityMasterRepository activityMasterRepository;

	@GetMapping("/add")
	public String showAddCompanyPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		List<ActivityMaster> activities = activityMasterRepository.findAll();
		model.addAttribute("activities", activities);
		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());
		model.addAttribute("currentPage", "companyAdd");

		return "companyAdd";
	}

	@GetMapping("/list")
	public String showCompanyListPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<Company> companies;

		if (loggedInUser.getRole().getRoleId() == 3L) {
			companies = companyService.getAllCompanies();
		} else {
			companies = companyService.getCompaniesCreatedByUser(loggedInUser.getEmail());
		}

		model.addAttribute("companies", companies);
		model.addAttribute("currentPage", "companyList");
		return "companyList";
	}

	@PostMapping("/saveCompany")
	public ResponseEntity<?> saveCompany(@ModelAttribute Company company, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			Optional<Company> existingCompany = companyService.findByName(company.getCompanyName());
			if (existingCompany.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status", "error", "message", "Company with the same name already exists"));
			}

			company.setCreatedBy(createdBy);
			company.setCreatedOn(LocalDateTime.now());
			company.setStatus("Active");

			Company savedCompany = companyService.saveCompany(company);

			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Company saved successfully", "company", savedCompany));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
		Company company = companyRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
		return ResponseEntity.ok(company);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateCompany(@PathVariable Long id,
			@ModelAttribute Company updatedCompany, HttpSession session) {

		try {
			User loggedInUser = (User) session.getAttribute("user");
			if (loggedInUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", "error", "message", "Unauthorized update"));
			}

			Company existingCompany = companyService.getCompanyById(id);
			if (existingCompany == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "Company not found"));
			}

			String updatedBy = loggedInUser.getEmail();
			Company savedCompany = companyService.updateCompany(id, updatedCompany, updatedBy);

			return ResponseEntity.ok(
					Map.of("status", "success", "message", "Company updated successfully!", "company", savedCompany));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	// @GetMapping("/edit/{id}")
	// public String EditCompany(@PathVariable("id") Long id, Model model,
	// HttpSession session) {
	// Company c1 = companyService.editcompany(id);
	// if (c1 == null) {
	// return "redirect:/companies/list?error=CompanyNotFound";
	// }

	// model.addAttribute("ap", c1);
	// return "EditCompany";
	// }

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteCompany(@PathVariable Long id, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			Company companyToDelete = companyService.getCompanyById(id);

			if (companyToDelete == null) {
				return ResponseEntity.ok(Map.of("status", "error", "message", "Company not found"));
			}

			if ("Super Admin".equalsIgnoreCase(companyToDelete.getCreatedBy())) {
				return ResponseEntity
						.ok(Map.of("status", "warning", "message", "Cannot delete company created by Super Admin"));
			}

			companyService.deleteCompany(id);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Company deleted successfully"));

		} catch (Exception e) {
			return ResponseEntity
					.ok(Map.of("status", "error", "message", "An error occurred while deleting the company"));
		}
	}

	@DeleteMapping({ "/delete" })
	public ResponseEntity<Map<String, String>> deleteSelectedCompanies(@RequestBody Map<String, List<Long>> payload,
			HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			List<Long> companyIds = payload.get("companyIds");

			if (companyIds == null || companyIds.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("warning", "No companies selected for deletion."));
			}

			for (Long id : companyIds) {
				Company companyToDelete = companyService.getCompanyById(id);
				if (companyToDelete == null) {
					continue;
				}

				if (loggedInUser != null && !companyToDelete.getCreatedBy().equals(loggedInUser.getEmail())
						&& loggedInUser.getRole().getRoleId() != 3L) {
					return ResponseEntity.badRequest()
							.body(Map.of("warning", "You can only delete companies created by you."));
				}

				companyService.deleteCompany(id);
			}

			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Selected companies deleted successfully."));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Something went wrong while deleting companies."));
		}
	}

	@PostMapping({ "/toggleStatus/{id}" })
	public ResponseEntity<Map<String, Object>> toggleCompanyStatus(@PathVariable Long id) {
		Company company = companyService.getCompanyById(id);

		if (company == null) {
			return ResponseEntity.status(404).body(Map.of("success", false, "message", "Company not found"));
		}

		if ("Super Admin".equals(company.getCreatedBy())) {
			return ResponseEntity.ok(Map.of("success", false, "warning", true, "message",
					"Super Admin created companies cannot be deactivated."));
		}

		Company updatedCompany = companyService.toggleCompanyStatus(id);

		return ResponseEntity.ok(Map.of("success", true, "newStatus", updatedCompany.getStatus()));
	}

}
