package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Plan;
import com.example.dop.Repository.PlanRepository;

@Service
public class PlanService {

	@Autowired
	private PlanRepository planRepository;

	public Plan savePlan(Plan plan) {

		plan.setPlanDate(LocalDateTime.now());
		plan.setStatus("Active");

		return planRepository.save(plan);
	}

	public long countPlans() {
		return planRepository.count();
	}

	public List<Plan> getAllPlans() {
		return planRepository.findAll();
	}

	public void deletePlans(List<Long> planId) {
		planRepository.deleteAllById(planId);
	}

	public void deletePlan(Long id) {
		if (planRepository.existsById(id)) {
			planRepository.deleteById(id);
		} else {
			throw new RuntimeException("Plan not found");
		}
	}

	public void deactivatePlan(Long id) {
		Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
		plan.setStatus("Inactive");
		planRepository.save(plan);
	}

	public Plan getPlanById(Long planId) {
		return planRepository.findById(planId).orElse(null);
	}

//	public Plan updatePlan(Long id, Plan updatedPlan) {
//		return planRepository.findById(id).map(existingPlan -> {
//			existingPlan.setPlanName(updatedPlan.getPlanName());
//			existingPlan.setPlanPrice(updatedPlan.getPlanPrice());
//			existingPlan.setProjectAuthorized(updatedPlan.getProjectAuthorized());
//			existingPlan.setFileRows(updatedPlan.getFileRows());
//			return planRepository.save(existingPlan);
//		}).orElseThrow(() -> new RuntimeException("Plan not found with id: " + id));
//	}

	public boolean existsByPlanName(String planName) {
		return planRepository.findByPlanName(planName).isPresent();
	}

	public Plan togglePlanStatus(Long id) {
		Optional<Plan> planOptional = planRepository.findById(id);
		if (planOptional.isPresent()) {
			Plan plan = planOptional.get();
			plan.setStatus(plan.getStatus().equals("Active") ? "Inactive" : "Active"); // Toggle Active/Inactive
			return planRepository.save(plan);
		}
		return null;
	}

}
