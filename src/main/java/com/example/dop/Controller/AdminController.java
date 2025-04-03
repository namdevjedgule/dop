package com.example.dop.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Admin;
import com.example.dop.Model.User;
import com.example.dop.Repository.AdminRepo;
import com.example.dop.Service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	@Autowired
	AdminService adminService;

	@Autowired
	AdminRepo adminRepo;

	@GetMapping("/admin/add")
	public String Add(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());
		return "AddAdmin";
	}

	@GetMapping("/admin/list")
	public String listAdmins(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusFilter", required = false) String statusFilter, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page, HttpSession session) {

		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());

		List<Admin> admins;

		if (keyword != null)
			keyword = keyword.trim();

		if (keyword != null && !keyword.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
			admins = adminService.searchAdminsByStatus(keyword, statusFilter);
		} else if (keyword != null && !keyword.isEmpty()) {
			admins = adminService.searchAdmins(keyword);
		} else if (statusFilter != null && !statusFilter.isEmpty()) {

			admins = adminService.getAdminsByStatus(statusFilter);
		} else {
			admins = adminService.getAdmins();
		}

		int pageSize = 5;

		Page<Admin> adminPage = adminService.getPaginatedSubscriptions(page, pageSize, keyword, statusFilter);

		model.addAttribute("admins", adminPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", adminPage.getTotalPages());
		model.addAttribute("admins", admins);
		model.addAttribute("keyword", keyword);
		model.addAttribute("statusFilter", statusFilter);

		return "AdminList";
	}

	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

	@PostMapping("/saveAdmin")
	public String saveAdmin(@ModelAttribute("a1") Admin a1, @RequestParam("profileImage") MultipartFile file)
			throws IOException {

		a1.setStatus("Active");
		adminService.savedata(a1, file);
		return "redirect:/admin/add?success";
	}

	@PostMapping("/deleteSelected")
	public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
			RedirectAttributes redirectAttributes) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			adminService.deleteAdmins(selectedIds);
			redirectAttributes.addFlashAttribute("message", "Selected admins deleted successfully.");
		} else {
			redirectAttributes.addFlashAttribute("error", "No admins selected for deletion.");
		}
		return "redirect:/admin/list";
	}

	@GetMapping("/edit/{id}")
	public String EditAdmin(@PathVariable("id") Long id, Model model, HttpSession session) {
		Admin a1 = adminService.editadmin(id);
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", loggedInUser.getFirstName());

		if (a1 == null) {
			return "redirect:/admin/list?error=AdminNotFound";
		}

		model.addAttribute("ap", a1);
		return "EditAdmin";
	}

	@PostMapping("/UpdateAdmin")
	public String updateAdmin(@ModelAttribute("ap") Admin ap, @RequestParam("profileImage") MultipartFile file)
			throws IOException {
		if (ap.getId() == null) {
			return "redirect:/ListAdmin?error=InvalidAdminId";
		}

		Admin existingAdmin = adminService.getAdminById(ap.getId());

		if (existingAdmin != null) {
			String existingStatus = existingAdmin.getStatus();

			existingAdmin.setFirstName(ap.getFirstName());
			existingAdmin.setLastName(ap.getLastName());
			existingAdmin.setEmail(ap.getEmail());

			if (ap.getPassword() != null && !ap.getPassword().trim().isEmpty()) {
				existingAdmin.setPassword(ap.getPassword());
			}

			existingAdmin.setStatus(existingStatus);

			adminService.savedata(existingAdmin, file);
		}

		return "redirect:/admin/list?success=AdminUpdated";
	}

	@GetMapping("/delete/{id}")
	public String del(@PathVariable("id") Long id) {
		adminService.deleteAdmin(id);
		return "redirect:/admin/list";
	}

	@GetMapping("/toggle-status/{id}")
	public String toggleAdminStatus(@PathVariable Long id) {
		adminService.toggleAdminStatus(id);
		return "redirect:/admin/list";
	}

	@PostMapping("/update-status/{id}/{newStatus}")
	@ResponseBody
	public Map<String, Object> updateAdminStatus(@PathVariable Long id, @PathVariable String newStatus) {
		Map<String, Object> response = new HashMap<>();
		try {
			Admin admin = adminService.getAdminById(id);
			if (admin != null) {
				admin.setStatus(newStatus);
				adminRepo.save(admin);
				response.put("success", true);
			} else {
				response.put("success", false);
			}
		} catch (Exception e) {
			response.put("success", false);
		}
		return response;
	}

}