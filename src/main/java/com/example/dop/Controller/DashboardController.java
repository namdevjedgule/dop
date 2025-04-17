package com.example.dop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dop.Model.User;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());
		model.addAttribute("userRole", session.getAttribute("userRole"));

		long totalAdmins = userService.countAdmins();
		long totalUsers = userService.countUsers();

		model.addAttribute("totalAdmins", totalAdmins);
		model.addAttribute("totalUsers", totalUsers);

		model.addAttribute("currentPage", "dashboard");
		return "dashboard";
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
//		model.addAttribute("role", loggedInUser.getRole() != null ? loggedInUser.getRole().getRoleName() : "N/A");
		model.addAttribute("status", loggedInUser.getStatus());
		model.addAttribute("createdBy", loggedInUser.getCreatedBy());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());
		model.addAttribute("userRole", session.getAttribute("userRole"));

		model.addAttribute("currentPage", "profile");
		return "profile";
	}

}