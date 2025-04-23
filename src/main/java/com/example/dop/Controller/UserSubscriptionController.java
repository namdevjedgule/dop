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

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Repository.UserRepository;
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

	@Autowired
	private SubRepo subRepo;

	@Autowired
	private UserRepository userRepository;

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

	@GetMapping("/editUserSubscription")
	public String showEditUserSubscriptionPage(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "userSubscriptionId", required = false) Long userSubscriptionId, HttpSession session,
			Model model) {

		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		User user = userService.getUserById(userId);
		model.addAttribute("user", user);
		model.addAttribute("userSubscription", new UserSubscription());

		return "editUserSubscription";
	}

	@PostMapping("/saveUserSubscription")
	public ResponseEntity<?> saveUserSubscription(@ModelAttribute UserSubscription userSubscription,
			@RequestParam("subscription.subscriptionId") Long subscriptionId, @RequestParam("user.userId") Long userId,
			HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = (loggedInUser != null) ? loggedInUser.getEmail() : "System";

			UserSubscription savedUserSubscription = userSubscriptionService.createUserSubscription(userSubscription,
					userId, subscriptionId, createdBy);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription added successfully",
					"userSubscription", savedUserSubscription));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@PostMapping("/updateUserSubscription/{userSubscriptionId}")
	public ResponseEntity<?> updateUserSubscription(@ModelAttribute UserSubscription userSubscription,
			@RequestParam("subscription.subscriptionId") Long subscriptionId, @RequestParam("user.userId") Long userId,
			HttpSession session) {

		try {
			User loggedInUser = (User) session.getAttribute("user");
			String updatedBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			System.out.println("Received userId for update: " + userId);
			System.out.println("Received userSubscriptionId for update: " + userSubscription.getUserSubscriptionId());

			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
			userSubscription.setUser(user);

			Subscription subscription = subRepo.findById(subscriptionId)
					.orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + subscriptionId));
			userSubscription.setSubscription(subscription);

			userSubscription.setUpdatedBy(updatedBy);
			userSubscription.setUpdatedDate(LocalDateTime.now());

			UserSubscription updatedSubscription = userSubscriptionService.updateUserSubscription(userSubscription);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription updated successfully",
					"userSubscription", updatedSubscription));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

}
