package com.example.dop.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {

	Optional<Plan> findByPlanName(String planName);

}
