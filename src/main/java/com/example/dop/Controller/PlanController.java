package com.example.dop.Controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dop.Model.Admin;
import com.example.dop.Model.Plan;
import com.example.dop.Service.PlanService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/plans")
@CrossOrigin("*")
public class PlanController {

	@Autowired
	private PlanService planService;

	@GetMapping("/add")
	public String showAddPlanPage(@RequestParam(value = "planId", required = false) Long planId, Model model,
			HttpSession session) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());

		model.addAttribute("currentPage", "planAdd");

		if (planId != null) {
			Plan plan = planService.getPlanById(planId);
			if (plan == null) {
				model.addAttribute("error", "Plan not found!");
				return "planId";
			}
			model.addAttribute("plan", plan);
			model.addAttribute("isEdit", true);
		} else {
			model.addAttribute("plan", new Plan());
			model.addAttribute("isEdit", false);
		}

		return "planAdd";
	}

	@GetMapping("/list")
	public String showPlanListPage(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("loggedInAdmin");

		if (admin == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", admin.getFirstName());
		List<Plan> plans = planService.getAllPlans();

		if (plans.isEmpty()) {
			System.out.println("No steps found in database.");
		} else {
			System.out.println("plans retrieved: " + plans.size());
		}

		model.addAttribute("plans", plans);
		model.addAttribute("currentPage", "planList");
		return "planList";
	}

	@PostMapping("/savePlan")
	public ResponseEntity<Map<String, Object>> savePlan(@RequestBody Plan plan) {
		try {
			if (planService.existsByPlanName(plan.getPlanName())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status", "error", "message", "Plan name already exists!"));
			}
			if (plan.getPlanDate() == null) {
				plan.setPlanDate(LocalDateTime.now());
			}

			Plan savedPlan = planService.savePlan(plan);

			return ResponseEntity
					.ok(Map.of("status", "success", "message", "Plan saved successfully!", "category", savedPlan));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", "Something went wrong!"));
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<Map<String, String>> deletePls(@RequestBody Map<String, List<Long>> request) {
		List<Long> planIds = request.get("planIds");
		planService.deletePlans(planIds);
		return ResponseEntity.ok(Collections.singletonMap("status", "success"));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deletePlan(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			planService.deletePlan(id);
			response.put("message", "Plan deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Plan not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@PutMapping("/update/{planId}")
	public ResponseEntity<Map<String, Object>> updatePlan(@PathVariable Long planId, @RequestBody Plan updatedPlan) {
		try {
			Plan existingPlan = planService.getPlanById(planId);
			if (existingPlan == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "Plan not found!"));
			}

			existingPlan.setPlanName(updatedPlan.getPlanName());
			existingPlan.setPlanPrice(updatedPlan.getPlanPrice());
			existingPlan.setProjectAuthorized(updatedPlan.getProjectAuthorized());
			existingPlan.setFileRows(updatedPlan.getFileRows());

			planService.savePlan(existingPlan);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Plan updated successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/{planId}")
	public ResponseEntity<Plan> getPlanById(@PathVariable Long planId) {
		Plan plan = planService.getPlanById(planId);

		if (plan != null) {
			return ResponseEntity.ok(plan);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/toggleStatus/{id}")
	public ResponseEntity<Map<String, Object>> togglePlanStatus(@PathVariable Long id) {
		Plan updatedPlan = planService.togglePlanStatus(id);

		if (updatedPlan != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("newStatus", updatedPlan.getStatus()); // Assuming `getStatus()` method exists in Plan entity
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(404).body(Map.of("success", false, "message", "Plan not found"));
	}

}
