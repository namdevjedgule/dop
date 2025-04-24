package com.example.dop.Controller;

import java.util.HashMap;
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
	public String showAddUserSubscriptionPage(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userSubscriptionId", required = false) Long userSubscriptionId, HttpSession session,
			Model model) {

		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		User user = userService.getUserById(userId);
		System.out.println("Fetched user: " + user);
		model.addAttribute("user", user);
		model.addAttribute("userSubscription", new UserSubscription());

		return "addUserSubscription";
	}

//	@GetMapping("/editUserSubscription")
//	public String showEditUserSubscriptionPage(@RequestParam(value = "userId", required = false) Long userId,
//			@RequestParam(value = "userSubscriptionId", required = false) Long userSubscriptionId, HttpSession session,
//			Model model) {
//		User loggedInUser = (User) session.getAttribute("user");
//		if (loggedInUser == null)
//			return "redirect:/";
//
//		model.addAttribute("fname", loggedInUser.getFirstName());
//		model.addAttribute("email", loggedInUser.getEmail());
//		model.addAttribute("picture", loggedInUser.getProfilePhoto());
//
//		User user = userService.getUserById(userId);
//		model.addAttribute("user", user);
//
//		return "editUserSubscription";
//	}

	@GetMapping("/editUserSubscription")
	public String showEditUserSubscriptionPage(@RequestParam(value = "userId") Long userId, HttpSession session,
			Model model) {

		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null) {
			return "redirect:/";
		}

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		User user = userService.getUserById(userId);
		model.addAttribute("user", user);

		UserSubscription userSubscription = userSubscriptionService.getUserSubscriptionByUserId(userId);
		if (userSubscription != null) {
			model.addAttribute("userSubscription", userSubscription);
		} else {
			model.addAttribute("userSubscription", new UserSubscription());
		}

		return "editUserSubscription";
	}

	@PostMapping("/saveUserSubscription")
	public ResponseEntity<?> saveUserSubscription(@ModelAttribute UserSubscription userSubscription,
			@RequestParam("subscription.subscriptionId") Long subscriptionId, @RequestParam("user.userId") Long userId,
			HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = (loggedInUser != null) ? loggedInUser.getEmail() : "System";

			UserSubscription savedUserSubscription = userSubscriptionService.saveUserSubscription(userSubscription,
					userId, subscriptionId, createdBy);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription added successfully",
					"userSubscription", savedUserSubscription));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@PostMapping("/updateUserSubscription")
	public ResponseEntity<?> updateUserSubscription(@ModelAttribute UserSubscription userSubscription,
			HttpSession session) {

		try {
			User loggedInUser = (User) session.getAttribute("user");
			String updatedBy = (loggedInUser != null) ? loggedInUser.getEmail() : "System";

			userSubscriptionService.updateUserSubscription(userSubscription, updatedBy);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription updated successfully"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/last-transaction-id")
	public ResponseEntity<Map<String, String>> getNextTransactionId() {
		String nextTransactionId = userSubscriptionService.generateNextTransactionId();

		Map<String, String> response = new HashMap<>();
		response.put("transactionId", nextTransactionId);

		return ResponseEntity.ok(response);
	}

}
