package com.example.dop.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		model.addAttribute("currentPage", "stepAdd");

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

	@GetMapping("/list")
	public String showStepListPage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<Step> steps = stepService.getAllSteps();

		if (steps.isEmpty()) {
			System.out.println("No steps found in database.");
		} else {
			System.out.println("steps retrieved: " + steps.size());
		}

		model.addAttribute("steps", steps);
		model.addAttribute("currentPage", "stepList");
		return "stepList";
	}

	@PostMapping("/saveStep")
	public ResponseEntity<Map<String, Object>> saveStep(@RequestParam("titleName") String titleName,
			@RequestParam(value = "icon") String icon,
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

	@PostMapping("/delete")
	public ResponseEntity<Map<String, String>> deleteSteps(@RequestBody Map<String, List<Long>> request) {
		List<Long> stepIds = request.get("stepIds");
		stepService.deleteSteps(stepIds);
		return ResponseEntity.ok(Collections.singletonMap("status", "success"));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteStep(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			stepService.deleteStep(id);
			response.put("message", "Step deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Step not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@PutMapping("/deactivate/{id}")
	public ResponseEntity<?> deactivateStep(@PathVariable Long id) {
		try {
			stepService.deactivateStep(id);
			return ResponseEntity.ok(Map.of("status", "success", "message", "Step deactivated successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/{stepId}")
	public ResponseEntity<?> getStepById(@PathVariable Long stepId) {
		System.out.println("Fetching step with ID: " + stepId);
		Step step = stepService.getStepById(stepId);

		if (step != null) {
			return ResponseEntity.ok(step);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/update/{stepId}")
	public ResponseEntity<Map<String, Object>> updateStep(@PathVariable Long stepId, @RequestBody Step updatedStep) {
		try {
			Step existingStep = stepService.getStepById(stepId);
			if (existingStep == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "Step not found!"));
			}

			existingStep.setTitleName(updatedStep.getTitleName());
			existingStep.setIcon(updatedStep.getIcon());
			existingStep.setAltTitle(updatedStep.getAltTitle());
			existingStep.setDescription(updatedStep.getDescription());
			existingStep.setStatus(updatedStep.getStatus());

			stepService.updateStep(existingStep);

			return ResponseEntity.ok(Map.of("status", "success", "message", "Step updated successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@PostMapping("/toggleStatus/{id}")
	public ResponseEntity<Map<String, Object>> toggleStepStatus(@PathVariable Long id) {
		Step updatedStep = stepService.toggleStepStatus(id);

		if (updatedStep != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("newStatus", updatedStep.getStatus());
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(404).body(Map.of("success", false, "message", "Step not found"));
	}

	@GetMapping("/images/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		try {
			Path path = Paths.get("uploads").resolve(filename);
			Resource resource = new UrlResource(path.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}

			String contentType = Files.probeContentType(path);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)) // Auto-detect image type
					.body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
