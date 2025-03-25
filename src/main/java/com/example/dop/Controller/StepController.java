package com.example.dop.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.Step;
import com.example.dop.Model.User;
import com.example.dop.Service.StepService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/steps")
@CrossOrigin("*")
public class StepController {

	@Autowired
	private StepService stepService;

	@GetMapping("/add")
	public String showAddStepPage(@RequestParam(value = "stepId", required = false) Long stepId, Model model,
			HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}

		model.addAttribute("currentPage", "stepAdd");
		model.addAttribute("fname", user.getFirstName());

		if (stepId != null) {
			Step step = stepService.getStepById(stepId);
			if (step == null) {
				model.addAttribute("error", "Step not found!");
				return "stepAdd";
			}
			model.addAttribute("step", step);
			model.addAttribute("isEdit", true);
		} else {
			model.addAttribute("step", new Step());
			model.addAttribute("isEdit", false);
		}

		return "stepAdd";
	}

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> saveStep(@RequestParam("titleName") String titleName,
			@RequestParam(value = "icon", required = false) String icon, // 👈 Make it optional
			@RequestParam(value = "altTitle", required = false) String altTitle,
			@RequestParam("description") String description, @RequestParam("imageFile") MultipartFile imageFile) {

		try {
			Step savedStep = stepService.saveStep(titleName, icon, altTitle, description, imageFile);
			Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
			response.put("message", "Step saved successfully!");
			response.put("step", savedStep);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", "Something went wrong!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
