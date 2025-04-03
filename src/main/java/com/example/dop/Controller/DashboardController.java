package com.example.dop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dop.Model.Admin;
import com.example.dop.Service.AdminService;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private PageService pageService;

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}

		long totalAdmins = adminService.countAdmins();
		long totalUsers = userService.countUsers();
//		long totalPages = pageService.countPages();

		model.addAttribute("totalAdmins", totalAdmins);
		model.addAttribute("totalUsers", totalUsers);
//		model.addAttribute("totalPages", totalPages);
		model.addAttribute("fname", admin.getFirstName());
		model.addAttribute("email", admin.getEmail());
		model.addAttribute("currentPage", "dashboard");
		return "dashboard";
	}

}