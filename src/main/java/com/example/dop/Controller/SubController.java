package com.example.dop.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Service.SubService;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/subscriptions")
public class SubController {

	@Autowired
	private SubService subService;

	@Autowired
	private UserService userService;

	@Autowired
	private SubRepo subRepo;

	@GetMapping("/all")
	@ResponseBody
	public List<Subscription> getAllSubscriptions() {
		return subRepo.findAll();
	}

	@GetMapping("/add")
	public String showAddSubscriptionPage(Model model, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());
		model.addAttribute("currentPage", "subscriptionAdd");

		return "subscriptionAdd";
	}

	@GetMapping("/list")
	public String showSubscriptionListPage(HttpSession session, Model model) {

		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

//		List<Subscription> subscriptions = subService.getSubscriptionsForUser(loggedInUser);
		List<Subscription> subscriptions = subService.getAllSubscriptions();

		model.addAttribute("subscriptions", subscriptions);
		model.addAttribute("currentPage", "subscriptionList");

		return "subscriptionList";
	}

	@PostMapping("/saveSubscription")
	public ResponseEntity<?> saveSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			subscription.setCreatedBy(createdBy);
			subscription.setCreatedOn(LocalDateTime.now());
			subscription.setStatus("Active");

			Subscription savedSubscription = subService.saveSubscription(subscription);

			return ResponseEntity.ok(
					Map.of("status", "success", "message", "Company saved successfully", "company", savedSubscription));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
		Subscription subscription = subRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
		return ResponseEntity.ok(subscription);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateSubscription(@PathVariable Long id,
			@ModelAttribute Subscription updatedSubscription, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			if (loggedInUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("status", "error", "message", "Unauthorized update"));
			}

			Subscription existingSubscription = subService.findById(id);
			if (existingSubscription == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "Subscription not found"));
			}

			String updatedBy = loggedInUser.getEmail();
			Subscription savedSubscription = subService.updateSubscription(id, updatedSubscription, updatedBy);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription updated successfully!",
					"subscription", savedSubscription));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable Long id, HttpSession session) {
		try {
			User loggedInUser = (User) session.getAttribute("user");
			Subscription subscriptionToDelete = subService.findById(id);

			subService.deleteSubscription(id);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Subscription deleted successfully"));

		} catch (Exception e) {
			return ResponseEntity
					.ok(Map.of("status", "error", "message", "An error occurred while deleting the subscription"));
		}
	}

	@PostMapping({ "/toggleStatus/{id}" })
	public ResponseEntity<Map<String, Object>> toggleSubscriptionStatus(@PathVariable Long id) {
		Subscription subscription = subService.getSubscriptionById(id);

		if (subscription == null) {
			return ResponseEntity.status(404).body(Map.of("success", false, "message", "Subscription not found"));
		}

//		if ("Super Admin".equals(subscription.getCreatedBy())) {
//			return ResponseEntity.ok(Map.of("success", false, "warning", true, "message",
//					"Super Admin created Subscription cannot be deactivated."));
//		}

		Subscription updatedSubscription = subService.toggleSubscriptionStatus(id);

		return ResponseEntity.ok(Map.of("success", true, "newStatus", updatedSubscription.getStatus()));
	}

}
