package com.example.dop.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dop.Model.User;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/add")
	public String showAddUserPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}

		model.addAttribute("fname", user.getFirstName());
		model.addAttribute("email", user.getUserEmailId());
		model.addAttribute("currentPage", "userAdd");
		return "userAdd";
	}

	@GetMapping("/list")
	public String showUserListPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}

		List<User> users = userService.getAllUsers();

		// Debugging
		if (users.isEmpty()) {
			System.out.println("No users found in database.");
		} else {
			System.out.println("Users retrieved: " + users.size());
		}

		model.addAttribute("users", users);
		model.addAttribute("fname", user.getFirstName());
		model.addAttribute("email", user.getUserEmailId());
		model.addAttribute("currentPage", "userList");
		return "userList";
	}

	@GetMapping("/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> user = userService.getUserByEmail(email);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/saveUser")
	public ResponseEntity<Map<String, Object>> saveUser(@RequestBody User user) {
		try {
			User savedUser = userService.saveUser(user);
			Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
			response.put("message", "User saved successfully!");
			response.put("user", savedUser);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", "Something went wrong!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}