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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dop.Model.Category;
import com.example.dop.Model.User;
import com.example.dop.Service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/add")
	public String showAddCategoryPage(@RequestParam(value = "categoryId", required = false) Long categoryId,
			Model model, HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}

		model.addAttribute("currentPage", "categoryAdd");
		model.addAttribute("fname", user.getFirstName());

		if (categoryId != null) {
			Category category = categoryService.getCategoryById(categoryId);
			if (category == null) {
				model.addAttribute("error", "Category not found!");
				return "categoryAdd"; // Ensure proper error handling
			}
			model.addAttribute("category", category);
			model.addAttribute("isEdit", true);
		} else {
			model.addAttribute("category", new Category());
			model.addAttribute("isEdit", false);
		}

		return "categoryAdd";
	}

	@GetMapping("/list")
	public String showCategoryListPage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");

		if (user == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", user.getFirstName());
		List<Category> categories = categoryService.getAllCategories();

		if (categories.isEmpty()) {
			System.out.println("No categories found in database.");
		} else {
			System.out.println("categories retrieved: " + categories.size());
		}

		model.addAttribute("categories", categories);
		model.addAttribute("currentPage", "categoryList");
		return "categoryList";
	}

	@PostMapping("/saveCategory")
	public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Category category) {
		try {
			// Check if category name already exists
			if (categoryService.existsByCategoryName(category.getCategoryName())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status", "error", "message", "Category name already exists!"));
			}

			// Ensure categoryDate is set
			if (category.getCategoryDate() == null) {
				category.setCategoryDate(LocalDateTime.now());
			}

			Category savedCategory = categoryService.saveCategory(category);

			return ResponseEntity.ok(
					Map.of("status", "success", "message", "Category saved successfully!", "category", savedCategory));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", "Something went wrong!"));
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<Map<String, String>> deleteCategories(@RequestBody Map<String, List<Long>> request) {
		List<Long> categoryIds = request.get("categoryIds");
		categoryService.deleteCategories(categoryIds);
		return ResponseEntity.ok(Collections.singletonMap("status", "success"));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			categoryService.deleteCategory(id);
			response.put("message", "Category deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", "Category not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@GetMapping("/edit/{id}")
	public String editCategory(@PathVariable Long id, Model model) {
		return "redirect:/categories/add?categoryId=" + id;
	}

	@PutMapping("/deactivate/{id}")
	public ResponseEntity<?> deactivateCategory(@PathVariable Long id) {
		try {
			categoryService.deactivateCategory(id);
			return ResponseEntity.ok(Map.of("status", "success", "message", "Category deactivated successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@PutMapping("/update/{categoryId}")
	public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long categoryId,
			@RequestBody Category updatedCategory) {
		try {
			Category existingCategory = categoryService.getCategoryById(categoryId);
			if (existingCategory == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status", "error", "message", "Category not found!"));
			}

			existingCategory.setCategoryName(updatedCategory.getCategoryName());
			categoryService.saveCategory(existingCategory); // Save updated category

			return ResponseEntity.ok(Map.of("status", "success", "message", "Category updated successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
		Category category = categoryService.getCategoryById(id);
		if (category != null) {
			return ResponseEntity.ok(category);
		} else {
			return ResponseEntity.notFound().build(); // 404 if not found
		}
	}

	@PostMapping("/toggleStatus/{id}")
	public ResponseEntity<Map<String, Object>> toggleCategoryStatus(@PathVariable Long id) {
		Category updatedCategory = categoryService.toggleCategoryStatus(id);

		if (updatedCategory != null) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("newStatus", updatedCategory.getStatus());
			return ResponseEntity.ok(response);
		}

		return ResponseEntity.status(404).body(Map.of("success", false, "message", "Category not found"));
	}

}
