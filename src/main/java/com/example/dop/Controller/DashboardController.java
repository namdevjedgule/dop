package com.example.dop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.dop.Model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}

		model.addAttribute("fname", user.getFirstName());
		model.addAttribute("email", user.getUserEmailId());
		model.addAttribute("currentPage", "dashboard");
		return "dashboard";
	}

}