package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Repository.UserRepository;

@Service
public class SubService {
	@Autowired
	SubRepo subRepo;

	@Autowired
	UserRepository userRepository;

	public List<Subscription> searchsubByStatus(String keyword, String statusFilter) {
		return null;
	}

	public List<Subscription> getSubByStatus(String status) {
		return subRepo.findByStatus(status);
	}

	public List<Subscription> getSubcriptions() {
		return subRepo.findAll();
	}

	public Subscription saveSubscription(Subscription subscription) {
		return subRepo.save(subscription);
	}

	public Subscription getSubscriptionById(Long id) {
		return subRepo.findById(id).orElse(null);
	}

	public void deleteSubscription(Long id) {
		if (subRepo.existsById(id)) {
			subRepo.deleteById(id);
		} else {
			throw new RuntimeException("Subscription not found!");
		}
	}

	public Subscription findById(Long id) {
		return subRepo.findById(id).orElse(null);
	}

	public void save(Subscription subscription) {
		subRepo.save(subscription);
	}

	public List<Subscription> getAllSubscriptions() {
		return subRepo.findAll();
	}

	public Subscription getById(Long id) {
		return subRepo.findById(id).orElse(null);
	}

	public List<Subscription> getSubscriptionsByCreatedBy(String email) {
		return subRepo.findByCreatedBy(email);
	}

	public List<Subscription> getSubscriptionsForUser(User loggedInUser) {
		String roleName = loggedInUser.getRole().getRoleName();
		String email = loggedInUser.getEmail();

		if ("SuperAdmin".equalsIgnoreCase(roleName)) {
			return subRepo.findAll();
		} else if ("Admin".equalsIgnoreCase(roleName)) {
			return subRepo.findByCreatedBy(loggedInUser.getEmail());
		} else {
			return Collections.emptyList();
		}
	}

	public Subscription updateSubscription(Long id, Subscription updatedSubscription, String updatedBy) {
		Subscription existingSubscription = subRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Subscription not found"));

		if (updatedSubscription.getSubscriptionName() != null) {
			existingSubscription.setSubscriptionName(updatedSubscription.getSubscriptionName());
		}

		if (updatedSubscription.getPrice() != null) {
			existingSubscription.setPrice(updatedSubscription.getPrice());
		}

		if (updatedSubscription.getProject() != null && !updatedSubscription.getProject().isBlank()) {
			existingSubscription.setProject(updatedSubscription.getProject());
		}

		if (updatedSubscription.getRowCount() != null && !updatedSubscription.getRowCount().isBlank()) {
			existingSubscription.setRowCount(updatedSubscription.getRowCount());
		}

		if (updatedSubscription.getDays() != null) {
			existingSubscription.setDays(updatedSubscription.getDays());
		}

		existingSubscription.setUpdatedBy(updatedBy);
		existingSubscription.setUpdatedOn(LocalDateTime.now());

		return subRepo.save(existingSubscription);
	}

	public Subscription toggleSubscriptionStatus(Long id) {
		Optional<Subscription> subOpt = subRepo.findById(id);

		if (subOpt.isPresent()) {
			Subscription subscription = subOpt.get();
			subscription.setStatus(subscription.getStatus().equals("Active") ? "Inactive" : "Active");
			return subRepo.save(subscription);
		}

		return null;
	}
}
