package com.example.dop.Controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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

import com.example.dop.Model.Company;
import com.example.dop.Model.SubNameMaster;
import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Service.SubService;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/subscription")
public class SubController 
{
	
	@Autowired
	SubService subService;
	
	@Autowired
	UserService userService;
	
	
	@GetMapping("/add")
	public String add(Model model) {
	    List<SubNameMaster> subNameMasters = subService.getAllSubNameMasters();
	    model.addAttribute("subNameMasters", subNameMasters);
	    model.addAttribute("subscription", new Subscription());
	    return "SubscriptionAdd";
	}

	
	@GetMapping("/list")
	public String listSubscriptions(
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "statusFilter", required = false) String statusFilter,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        Model model) {
	    
	    List<Subscription> subs;

	    // Remove leading/trailing spaces from keyword
	    if (keyword != null) keyword = keyword.trim();

	    // Check conditions and call appropriate service method
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
	    int pageSize = 5; // Number of items per page

	    Page<Subscription> subscriptionPage = subService.getPaginatedSubscriptions(page, pageSize, keyword, statusFilter);

	    model.addAttribute("subscriptions", subscriptionPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", subscriptionPage.getTotalPages());
	    

	    return "SubscriptionList"; // Your template name
	}



	
	@PostMapping("/save")
	public String saveSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
	    Long userId = (Long) session.getAttribute("userId");

	    if (userId == null) {
	        return "redirect:/login";
	    }

	    User user = userService.findById(userId);
	    subscription.setCreatedBy(user);
	    subscription.setStatus("Active");

	    subService.saveSubscription(subscription, user);
	    return "redirect:/subscription/add";
	}

	@GetMapping("/edit/{id}")
	public String editSubscription(@PathVariable("id") Long id, Model model) { 
	    Subscription subscription = subService.findById(id);
	    List<SubNameMaster> subNameMasters = subService.getAllSubNameMasters(); // Fetch dropdown options

	    model.addAttribute("subscription", subscription);
	    model.addAttribute("subNameMasters", subNameMasters); // Send list to frontend

	    return "EditSubscription";
	}



	@PostMapping("/update")
	public String updateSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        return "redirect:/login";
	    }
	    User user = userService.findById(userId);
	    subscription.setCreatedBy(user);
	    subService.saveSubscription(subscription, user);
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
