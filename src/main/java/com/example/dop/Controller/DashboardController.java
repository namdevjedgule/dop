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
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		long totalAdmins = userService.countAdmins();
		long totalUsers = userService.countUsers();

		model.addAttribute("totalAdmins", totalAdmins);
		model.addAttribute("totalUsers", totalUsers);

		model.addAttribute("currentPage", "dashboard");
		return "dashboard";
	}

}