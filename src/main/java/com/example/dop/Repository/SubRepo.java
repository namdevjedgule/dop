package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Subscription;

public interface SubRepo extends JpaRepository<Subscription, Long> {

	List<Subscription> findByStatus(String status);

	List<Subscription> findByCreatedBy(String createdBy);

}
