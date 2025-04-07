package com.example.dop.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dop.Model.SubNameMaster;
import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Service.SubService;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/subscription")
public class SubController {

	@Autowired
	SubService subService;

	@Autowired
	UserService userService;

	@GetMapping("/add")
	public String add(Model model, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<SubNameMaster> subNameMasters = subService.getAllSubNameMasters();
		model.addAttribute("subNameMasters", subNameMasters);
		model.addAttribute("subscription", new Subscription());
		return "SubscriptionAdd";
	}

	@GetMapping("/list")
	public String listSubscriptions(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusFilter", required = false) String statusFilter,
			@RequestParam(value = "page", defaultValue = "0") int page, Model model, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInAdmin");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<Subscription> subs;

		if (keyword != null)
			keyword = keyword.trim();

		if (keyword != null && !keyword.isEmpty()) {
			subs = subService.searchSubByKeyword(keyword);
		} else if (statusFilter != null && !statusFilter.isEmpty()) {
			subs = subService.getSubByStatus(statusFilter);
		} else {
			subs = subService.getSubcriptions();
		}

		model.addAttribute("subs", subs);
		model.addAttribute("keyword", keyword);
		model.addAttribute("statusFilter", statusFilter);
		int pageSize = 5;

		Page<Subscription> subscriptionPage = subService.getPaginatedSubscriptions(page, pageSize, keyword,
				statusFilter);

		model.addAttribute("subscriptions", subscriptionPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", subscriptionPage.getTotalPages());

		return "SubscriptionList";
	}

	@PostMapping("/save")
	public String saveSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
	    User loggedInAdmin = (User) session.getAttribute("loggedInAdmin");
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    String createdByEmail = null;

	    if (loggedInAdmin != null) {
	        createdByEmail = loggedInAdmin.getEmail();
	    } else if (loggedInUser != null) {
	        createdByEmail = loggedInUser.getEmail();
	    } else {
	        System.out.println("No user or admin logged in. Redirecting to login.");
	        return "redirect:/login";
	    }

	    try {
	       	subscription.setCreatedBy(createdByEmail);
	        subscription.setCreatedOn(new java.sql.Timestamp(System.currentTimeMillis()));
	        subscription.setStatus("Active");

	        subService.saveSubscription(subscription);
	        return "redirect:/subscription/add";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/error";
	    }
	}


	@GetMapping("/edit/{id}")
	public String editSubscription(@PathVariable("id") Long id, Model model, HttpSession session) {
		

		Subscription subscription = subService.findById(id);
		List<SubNameMaster> subNameMasters = subService.getAllSubNameMasters();

		model.addAttribute("subscription", subscription);
		model.addAttribute("subNameMasters", subNameMasters);

		return "EditSubscription";
	}

	@PostMapping("/update")
	public String updateSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
	    User loggedInAdmin = (User) session.getAttribute("loggedInAdmin");
	    User loggedInUser = (User) session.getAttribute("loggedInUser");

	    String updatedByEmail = null;

	    if (loggedInAdmin != null) {
	        updatedByEmail = loggedInAdmin.getEmail();
	    } else if (loggedInUser != null) {
	        updatedByEmail = loggedInUser.getEmail();
	    } else {
	        return "redirect:/login";
	    }

	    Subscription existing = subService.getById(subscription.getId());

	    if (existing == null) {
	        return "redirect:/error"; 
	    }

	
	    subscription.setCreatedBy(existing.getCreatedBy());
	    subscription.setCreatedOn(existing.getCreatedOn());

	    subscription.setStatus("Active");
	    subscription.setUpdatedBy(updatedByEmail);
	    subscription.setUpdatedOn(new java.sql.Timestamp(System.currentTimeMillis()));

	    subService.saveSubscription(subscription);
	    return "redirect:/subscription/list";
	}

	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteSubscription(@PathVariable("id") Long id) {
		try {
			subService.deleteSubscription(id);
			return ResponseEntity.ok("Subscription deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting subscription");
		}
	}

	@PostMapping("/toggle-status/{id}")
	@ResponseBody
	public ResponseEntity<String> toggleSubscriptionStatus(@PathVariable("id") Long id) {
		try {
			Subscription sub = subService.findById(id);
			if (sub != null) {
				sub.setStatus(sub.getStatus().equals("Active") ? "Inactive" : "Active");
				subService.save(sub);
				return ResponseEntity.ok("Subscription status updated successfully");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscription not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status");
		}
	}

}
