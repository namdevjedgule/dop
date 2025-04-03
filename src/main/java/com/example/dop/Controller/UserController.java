package com.example.dop.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dop.Model.Admin;
import com.example.dop.Model.User;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/add")
	public String showAddUserPage(@RequestParam(value = "userId", required = false) Long userId, HttpSession session,
			Model model) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());
		model.addAttribute("email", admin.getEmail());
		model.addAttribute("currentPage", "userAdd");

		if (userId != null) {
			User user = userService.getUserById(userId);
			if (user == null) {
				model.addAttribute("error", "User not found!");
				return "userAdd";
			}
			model.addAttribute("user", user);
			model.addAttribute("isEdit", true);
		} else {
			model.addAttribute("user", new User());
			model.addAttribute("isEdit", false);
		}

		return "userAdd";
	}

	@GetMapping("/list")
	public String showUserListPage(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());
		model.addAttribute("email", admin.getEmail());

		List<User> users = new ArrayList<>(); // Always initialize to avoid null issues
		try {
			users = userService.getAllUsers();
			System.out.println("Users retrieved: " + users.size());
		} catch (Exception e) {
			System.err.println("Error fetching users: " + e.getMessage());
		}

		if (users.isEmpty()) {
			System.out.println("No users found in database.");
		} else {
			System.out.println("Users retrieved: " + users.size());
		}

		model.addAttribute("users", users);

		model.addAttribute("currentPage", "userList");
		return "userList";
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
			e.printStackTrace(); // This prints the actual error in the backend logs

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", "Some error occurred: " + e.getMessage()); // Show actual error

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			userService.deleteUser(id);
			response.put("message", "Category deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Category not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@PostMapping("/toggleStatus/{id}")
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@PathVariable Long id) {
		User updatedUser = userService.toggleUserStatus(id);

		if (updatedUser != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("newStatus", updatedUser.getStatus());
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found"));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {
		User user = userService.getUserById(userId);

		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
		try {
			User existingUser = userService.getUserById(userId);
			if (existingUser == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "User not found!"));
			}

			existingUser.setFirstName(updatedUser.getFirstName());
			existingUser.setLastName(updatedUser.getLastName());
			existingUser.setEmail(updatedUser.getEmail());

			if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
				existingUser.setPassword(updatedUser.getPassword());
			}

			userService.saveUser(existingUser);

			return ResponseEntity.ok(Map.of("status", "success", "message", "User updated successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/check-email")
	public ResponseEntity<?> checkEmail(@RequestParam String email) {
		boolean exists = userService.checkIfEmailExists(email);
		return ResponseEntity.ok(Collections.singletonMap("exists", exists));
	}

}