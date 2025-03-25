package com.example.dop.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dop.Model.Step;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

	Optional<Step> findByTitleName(String titleName);
}
