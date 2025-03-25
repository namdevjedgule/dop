package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Category;
import com.example.dop.Repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Category saveCategory(Category category) {

		category.setCategoryDate(LocalDateTime.now());
		category.setStatus("Active");

		return categoryRepository.save(category);
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public void deleteCategories(List<Long> categoryIds) {
		categoryRepository.deleteAllById(categoryIds);
	}

	public void deleteCategory(Long id) {
		if (categoryRepository.existsById(id)) {
			categoryRepository.deleteById(id);
		} else {
			throw new RuntimeException("Category not found");
		}
	}

	public void deactivateCategory(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));
		category.setStatus("Inactive");
		categoryRepository.save(category);
	}

	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	public boolean existsByCategoryName(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName).isPresent();
	}

	public Category toggleCategoryStatus(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);

		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			category.setStatus(category.getStatus().equals("Active") ? "Inactive" : "Active");
			return categoryRepository.save(category);
		}

		return null;
	}

}
