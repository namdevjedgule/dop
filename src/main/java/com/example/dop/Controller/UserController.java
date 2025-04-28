package com.example.dop.Controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.User;
import com.example.dop.Repository.RoleMasterRepository;
import com.example.dop.Repository.UserRepository;
import com.example.dop.Service.UserService;
import com.example.dop.Service.UserSubscriptionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserSubscriptionService userSubscriptionService;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private UserRepository userRepository;

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
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null) {
			return "redirect:/";
		}

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
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<User> admins = userService.getAdminsAndSuperAdmins();

		admins.sort((u1, u2) -> {
			if (u1.getRole().getRoleId() == 3)
				return -1;
			else if (u2.getRole().getRoleId() == 3)
				return 1;
			return u1.getFirstName().compareToIgnoreCase(u2.getFirstName());
		});

		model.addAttribute("admins", admins);
		model.addAttribute("currentPage", "adminList");
		return "adminList";
	}

//	@GetMapping("/users/list")
//	public String showUserListPage(HttpSession session, Model model) {
//		User loggedInUser = (User) session.getAttribute("user");
//		if (loggedInUser == null)
//			return "redirect:/";
//
//		model.addAttribute("fname", loggedInUser.getFirstName());
//		model.addAttribute("email", loggedInUser.getEmail());
//		model.addAttribute("picture", loggedInUser.getProfilePhoto());
//
//		List<User> users = null;
//		if (loggedInUser.getRole().getRoleId() == 3) {
//			users = userService.getUsersByRoleId(2L);
//			model.addAttribute("users", users);
//		} else {
//			List<User> usersCreatedByAdmin = userService.getUsersCreatedByAdmin(loggedInUser.getEmail());
//			model.addAttribute("users", usersCreatedByAdmin);
//		}
//
//		if (users != null) {
//			for (User user : users) {
//				if (user.getUserSubscription() != null && user.getUserSubscription().getSubscription() != null) {
//					user.setSubscriptionName(user.getUserSubscription().getSubscription().getSubscriptionName());
//				} else {
//					user.setSubscriptionName("free");
//				}
//			}
//		}
//
//		model.addAttribute("users", users);
//
//		model.addAttribute("currentPage", "userList");
//		return "userList";
//	}

	@GetMapping("/users/list")
	public String showUserListPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null) {
			return "redirect:/";
		}

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<User> users;
		if (loggedInUser.getRole().getRoleId() == 3) {
			users = userService.getUsersByRoleId(2L);
		} else {
			users = userService.getUsersCreatedByAdmin(loggedInUser.getEmail());
		}

		for (User user : users) {
			if (user.getUserSubscription() != null && user.getUserSubscription().getSubscription() != null) {
				user.setSubscriptionName(user.getUserSubscription().getSubscription().getSubscriptionName());
//				user.setStatus(user.getUserSubscription().getStatus());
				user.setSubscriptionStatus(user.getUserSubscription().getStatus());
				user.setHasSubscription("Active".equalsIgnoreCase(user.getUserSubscription().getStatus()));
			} else {
				user.setSubscriptionName("free");
				user.setStatus("Active");
				user.setHasSubscription(true);
			}

			user.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : "N/A");
			user.setDesignationName(user.getDesignation() != null ? user.getDesignation().getDesignationName() : "N/A");
			user.setCompanyName(user.getCompany() != null ? user.getCompany().getCompanyName() : "N/A");
			user.setCountryName(user.getCountry() != null ? user.getCountry().getCountryName() : "N/A");
			user.setAbout(user.getAboutUs() != null ? user.getAboutUs().getDescription() : "N/A");
			user.setCreatedBy(user.getCreatedBy());
			user.setCreatedDate(user.getCreatedDate());
			user.setUpdatedBy(user.getUpdatedBy());
			user.setUpdatedDate(user.getUpdatedDate());
			user.setMemberSince(user.getMemberSince());
			user.setLastLogin(user.getLastLogin());
			user.setCreatedByType(user.getCreatedByType());
		}

		model.addAttribute("users", users);
		model.addAttribute("currentPage", "userList");

		return "userList";
	}

//	@GetMapping("/users/list")
//	public String showUserListPage(HttpSession session, Model model) {
//		User loggedInUser = (User) session.getAttribute("user");
//		if (loggedInUser == null)
//			return "redirect:/";
//
//		model.addAttribute("fname", loggedInUser.getFirstName());
//		model.addAttribute("email", loggedInUser.getEmail());
//		model.addAttribute("picture", loggedInUser.getProfilePhoto());
//
//		List<User> users;
//		if (loggedInUser.getRole().getRoleId() == 3) {
//			users = userService.getUsersByRoleId(2L);
//		} else {
//			users = userService.getUsersCreatedByAdmin(loggedInUser.getEmail());
//		}
//		model.addAttribute("users", users);
//
//		List<UserSubscription> userSubscriptions = userSubscriptionService.getAllUserSubscriptions();
//		model.addAttribute("subscriptions", userSubscriptions);
//
//		model.addAttribute("currentPage", "userList");
//		return "userList";
//	}

	@PostMapping("/admins/saveAdmin")
	public ResponseEntity<?> saveAdmin(@ModelAttribute User admin,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

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
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			User savedUser = userService.saveUserWithFile(user, imageFile, "User", createdBy);
			return ResponseEntity
					.ok(Map.of("status", "success", "message", "User saved successfully", "user", savedUser));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@DeleteMapping({ "/admins/delete/{id}", "/users/delete/{id}" })
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			User userToDelete = userService.getUserById(id);

			if (userToDelete == null) {
				return ResponseEntity.ok(Map.of("status", "error", "message", "User not found"));
			}

			if (userToDelete.getRole().getRoleId() == 3L) {
				return ResponseEntity.ok(Map.of("status", "warning", "message", "Super Admin cannot be deleted"));
			}

			if (loggedInUser != null && loggedInUser.getId().equals(id)) {
				return ResponseEntity.ok(Map.of("status", "warning", "message", "You cannot delete your own account"));
			}

			long adminCount = userService.countByRole("Admin");
			if (userToDelete.getRole().getRoleName().equals("Admin") && adminCount <= 1) {
				return ResponseEntity
						.ok(Map.of("status", "warning", "message", "Cannot delete the last remaining admin."));
			}

			userService.deleteUser(id);

			String roleName = userToDelete.getRole().getRoleName();
			return ResponseEntity.ok(Map.of("status", "success", "message", roleName + " deleted successfully"));

		} catch (Exception e) {
			return ResponseEntity.ok(Map.of("status", "error", "message", "An error occurred"));
		}
	}

	@DeleteMapping({ "/admins/delete", "/users/delete" })
	public ResponseEntity<Map<String, String>> deleteSelectedUsers(@RequestBody Map<String, List<Long>> payload,
			HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			List<Long> userIds = payload.get("userIds");

			if (userIds == null || userIds.isEmpty()) {
				return ResponseEntity.badRequest().body(Map.of("warning", "No users selected for deletion."));
			}

			for (Long id : userIds) {
				User userToDelete = userService.getUserById(id);
				if (userToDelete == null)
					continue;

				if (userToDelete.getRole().getRoleId() == 3L) {
					return ResponseEntity.badRequest().body(Map.of("warning", "Super Admin cannot be deleted."));
				}

				if (loggedInUser != null && loggedInUser.getId().equals(id)) {
					return ResponseEntity.badRequest().body(Map.of("warning", "You cannot delete your own account."));
				}

				if ("Admin".equals(userToDelete.getRole().getRoleName()) && userService.countByRole("Admin") <= 1) {
					return ResponseEntity.badRequest().body(Map.of("error", "Cannot delete the last remaining admin."));
				}

				userService.deleteUser(id);
			}

			return ResponseEntity.ok(Map.of("status", "success", "message", "Selected users deleted successfully."));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Something went wrong while deleting users."));
		}
	}

	@PostMapping({ "/admins/toggleStatus/{id}", "/users/toggleStatus/{id}" })
	public ResponseEntity<Map<String, Object>> toggleUserStatus(@PathVariable Long id) {
		User user = userService.getUserById(id);

		if (user == null) {
			return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found"));
		}

		if (user.getRole().getRoleId() == 3L) {
			return ResponseEntity
					.ok(Map.of("success", false, "warning", true, "message", "Super Admin cannot be deactivated."));
		}

		User updatedUser = userService.toggleUserStatus(id);

		return ResponseEntity.ok(Map.of("success", true, "newStatus", updatedUser.getStatus()));
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
			User loggedInUser = (User) session.getAttribute("user");
			if (loggedInUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", "error", "message", "Unauthorized update"));
			}

			User existingUser = userService.getUserById(id);
			if (existingUser != null && existingUser.getRole().getRoleId() == 3L) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("status", "warning", "message", "Super Admin cannot be edited."));
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
	public ResponseEntity<?> checkEmail(@RequestParam String email, @RequestParam(required = false) Long userId) {
		boolean exists = userService.isEmailTaken(email, userId);
		return ResponseEntity.ok(Map.of("exists", exists));
	}

	@PostMapping("/checkEmail")
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		boolean exists = userRepository.existsByEmail(email);

		Map<String, Object> response = new HashMap<>();
		if (exists) {
			response.put("success", false);
			response.put("message", "Email already exists");
		} else {
			response.put("success", true);
			response.put("message", "Email is available");
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email,
			@RequestParam("status") String status,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			if (loggedInUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", "error", "message", "Unauthorized update"));
			}

			User updatedUser = userService.updateProfile(loggedInUser, firstName, lastName, email, status, imageFile);

			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Profile updated successfully!", "user", updatedUser));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

}
