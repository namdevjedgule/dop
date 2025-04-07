package com.example.dop.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.User;
import com.example.dop.Repository.RoleMasterRepository;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@GetMapping("/admins/add")
	public String showAddAdminPage(@RequestParam(value = "userId", required = false) Long userId, HttpSession session,
			Model model) {
		return loadAddPage(userId, session, model, 1);
	}

	@GetMapping("/users/add")
	public String showAddUserPage(@RequestParam(value = "userId", required = false) Long userId, HttpSession session,
			Model model) {
		return loadAddPage(userId, session, model, 2);
	}

	private String loadAddPage(Long userId, HttpSession session, Model model, int roleId) {
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		if (userId != null) {
			User user = userService.getUserById(userId);
			model.addAttribute("user", user != null ? user : new User());
			model.addAttribute("isEdit", user != null);
		} else {
			model.addAttribute("user", new User());
			model.addAttribute("isEdit", false);
		}

		model.addAttribute("roleId", roleId);
		model.addAttribute("currentPage", roleId == 1 ? "adminAdd" : "userAdd");
		return roleId == 1 ? "adminAdd" : "userAdd";
	}

	@GetMapping("/admins/list")
	public String showAdminListPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<User> admins = userService.getUsersByRoleId(1L);

		for (User admin : admins) {
			System.out.println(admin.getFirstName() + " - " + admin.getEmail());
		}
		model.addAttribute("admins", admins);

		model.addAttribute("currentPage", "adminList");
		return "adminList";
	}

	@GetMapping("/users/list")
	public String showUserListPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<User> users = userService.getUsersByRoleId(2L);
		System.out.println("Total users: " + users.size());

		for (User user : users) {
			System.out.println(user.getFirstName() + " - " + user.getEmail());
		}
		model.addAttribute("users", users);

		model.addAttribute("currentPage", "userList");
		return "userList";
	}

	@PostMapping("/admins/saveAdmin")
	public ResponseEntity<?> saveAdmin(@ModelAttribute User admin,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session) {
		try {
			User loggedInAdmin = (User) session.getAttribute("loggedInAdmin");
			String createdBy = loggedInAdmin != null ? loggedInAdmin.getEmail() : "System";

			User savedAdmin = userService.saveUserWithFile(admin, imageFile, "Admin", createdBy);
			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Admin saved successfully", "user", savedAdmin));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@PostMapping("/users/saveUser")
	public ResponseEntity<?> saveRegularUser(@ModelAttribute User user,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session) {
		try {
			User loggedInAdmin = (User) session.getAttribute("loggedInAdmin");
			String createdBy = loggedInAdmin != null ? loggedInAdmin.getEmail() : "System";

			User savedUser = userService.saveUserWithFile(user, imageFile, "User", createdBy);
			return ResponseEntity
					.ok(Map.of("status", "success", "message", "User saved successfully", "user", savedUser));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@DeleteMapping({ "/admins/delete/{id}", "/users/delete/{id}" })
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
		}
	}

	@PostMapping({ "/admins/toggleStatus/{id}", "/users/toggleStatus/{id}" })
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@PathVariable Long id) {
		User updatedUser = userService.toggleUserStatus(id);
		if (updatedUser != null) {
			return ResponseEntity.ok(Map.of("success", true, "newStatus", updatedUser.getStatus()));
		}
		return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found"));
	}

	@GetMapping({ "/admins/{id}", "/users/{id}" })
	@ResponseBody
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
	}

	@PutMapping(value = { "/admins/update/{id}", "/users/update/{id}" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @ModelAttribute User updatedUser,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("loggedInAdmin");
			if (loggedInUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", "error", "message", "Unauthorized update"));
			}

			String updatedBy = loggedInUser.getEmail();

			User savedUser = userService.updateUserWithFile(id, updatedUser, imageFile, updatedBy);

			return ResponseEntity
					.ok(Map.of("status", "success", "message", "User updated successfully!", "user", savedUser));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/check-email")
	public ResponseEntity<?> checkEmail(@RequestParam String email) {
		boolean exists = userService.checkIfEmailExists(email);
		return ResponseEntity.ok(Map.of("exists", exists));
	}
}
