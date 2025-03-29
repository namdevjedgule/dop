package com.example.dop.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.Step;
import com.example.dop.Repository.StepRepository;

@Service
public class StepService {

	@Autowired
	private StepRepository stepRepository;

	private final String UPLOAD_DIR = "uploads/";

	public List<Step> getAllSteps() {
		return stepRepository.findAll();
	}

	public void deleteSteps(List<Long> stepIds) {
		stepRepository.deleteAllById(stepIds);
	}

	public Step getStepById(Long id) {
		return stepRepository.findById(id).orElse(null);
	}

	public Step saveStep(String titleName, String icon, String altTitle, String description, MultipartFile imageFile) {
		try {
			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists())
				uploadDir.mkdirs();

			String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
			Path filePath = Paths.get(UPLOAD_DIR, fileName);
			Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			Step step = new Step();
			step.setTitleName(titleName);
			step.setIcon(icon);
			step.setAltTitle(altTitle);
			step.setDescription(description);
			step.setImageUrl(fileName);
			step.setStepDate(LocalDateTime.now());
			step.setStatus("Active");

			return stepRepository.save(step);
		} catch (IOException e) {
			throw new RuntimeException("Image upload failed!", e);
		}
	}

	public void deleteStep(Long id) {
		if (stepRepository.existsById(id)) {
			stepRepository.deleteById(id);
		} else {
			throw new RuntimeException("Step not found");
		}
	}

	public void deactivateStep(Long id) {
		Step step = stepRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
		step.setStatus("Inactive");
		stepRepository.save(step);
	}

	public boolean existsByStepName(String stepName) {
		return stepRepository.findByTitleName(stepName).isPresent();
	}

	public Step toggleStepStatus(Long id) {
		Optional<Step> stepOptional = stepRepository.findById(id);

		if (stepOptional.isPresent()) {
			Step step = stepOptional.get();
			step.setStatus(step.getStatus().equals("Active") ? "Inactive" : "Active");
			return stepRepository.save(step);
		}

		return null;
	}

	public Step updateStep(Step step) {
		return stepRepository.save(step);
	}
}
