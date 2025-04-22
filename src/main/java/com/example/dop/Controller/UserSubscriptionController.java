package com.example.dop.Controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;
import com.example.dop.Service.UserService;
import com.example.dop.Service.UserSubscriptionService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserSubscriptionController {

	@Autowired
	private UserSubscriptionService userSubscriptionService;

	@Autowired
	private UserService userService;

	@GetMapping("/addUserSubscription")
	public String showAddUserSubscriptionPage(@RequestParam("userId") Long userId, HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

//		Long userId1 = Long.parseLong(userId);
		User user = userService.getUserById(userId);
		System.out.println("Fetched user for subscription: " + user);
		model.addAttribute("user", user);
		model.addAttribute("userSubscription", new UserSubscription());

		return "addUserSubscription";
	}

	@PostMapping("/saveUserSubscription")
	public ResponseEntity<?> saveUserSubscription(@ModelAttribute UserSubscription userSubscription,
			HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			userSubscription.setCreatedBy(createdBy);
			userSubscription.setCreatedDate(LocalDateTime.now());

			UserSubscription savedserSubscription = userSubscriptionService.saveUserSubscription(userSubscription);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription added successfully",
					"userSubscription", savedserSubscription));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

//	@PostMapping("/saveUserSubscription")
//	public ResponseEntity<?> saveUserSubscription(@ModelAttribute UserSubscription userSubscription,
//			HttpSession session) {
//		try {
//			User loggedInUser = (User) session.getAttribute("user");
//			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";
//
//			userSubscription.setCreatedBy(createdBy);
//			userSubscription.setCreatedDate(LocalDateTime.now());
//
//			if (loggedInUser != null) {
//				userSubscription.setUser(loggedInUser);
//			}
//
//			UserSubscription savedSubscription = userSubscriptionService.saveUserSubscription(userSubscription);
//
//			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription added successfully",
//					"userSubscription", savedSubscription));
//		} catch (EntityNotFoundException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(Map.of("status", "error", "message", e.getMessage()));
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(Map.of("status", "error", "message", e.getMessage()));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(Map.of("status", "error", "message", "An unexpected error occurred"));
//		}
//	}

}
