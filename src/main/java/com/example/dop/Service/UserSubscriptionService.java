package com.example.dop.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Repository.UserRepository;
import com.example.dop.Repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionService {

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubRepo subRepo;

	public String generateNextTransactionId() {
		String prefix = "TXN";
		String startingPoint = "1111111110";

		Optional<UserSubscription> latest = userSubscriptionRepository.findTopByOrderByUserSubscriptionIdDesc();
		String lastId = latest.map(UserSubscription::getTransactionId).orElse(prefix + startingPoint);

		String lastIdNumber = lastId.replace(prefix, "");
		long nextIdNumber = Long.parseLong(lastIdNumber) + 1;

		String nextId = String.format("%010d", nextIdNumber);

		return prefix + nextId;
	}

	public UserSubscription saveUserSubscription(UserSubscription userSubscription, Long userId, Long subscriptionId,
			String createdBy) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		Subscription subscription = subRepo.findById(subscriptionId)
				.orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + subscriptionId));

		if (existsByUserAndSubscription(user, subscription)) {
			throw new RuntimeException("Subscription already exists for this user!");
		}

		String transactionId = generateNextTransactionId();
		System.out.println("Generated Transaction ID: " + transactionId);
		userSubscription.setTransactionId(transactionId);

		userSubscription.setUser(user);
		userSubscription.setSubscription(subscription);
		userSubscription.setCreatedBy(createdBy);
		userSubscription.setCreatedDate(LocalDateTime.now());

		return userSubscriptionRepository.save(userSubscription);
	}

	public Subscription getSubscriptionById(Long subscriptionId) {
		return subRepo.findById(subscriptionId).orElseThrow(() -> new RuntimeException("Subscription not found"));
	}

	public boolean existsByUserAndSubscription(User user, Subscription subscription) {
		return userSubscriptionRepository.existsByUserAndSubscription(user, subscription);
	}

	public UserSubscription updateUserSubscription(UserSubscription userSubscription, String updatedBy) {
		UserSubscription existingSubscription = userSubscriptionRepository
				.findById(userSubscription.getUserSubscriptionId())
				.orElseThrow(() -> new RuntimeException("User Subscription not found"));

		existingSubscription.setProjectAuthorized(userSubscription.getProjectAuthorized());
		existingSubscription.setFileRows(userSubscription.getFileRows());

		if (userSubscription.getStatus() != null) {
			existingSubscription.setStatus(userSubscription.getStatus());
		} else {
			existingSubscription.setStatus("Active");
		}

		existingSubscription.setUpdatedBy(updatedBy);
		existingSubscription.setUpdatedDate(LocalDateTime.now());

		return userSubscriptionRepository.save(existingSubscription);
	}

	public UserSubscription getUserSubscriptionByUserId(Long id) {
		return userSubscriptionRepository.findByUserId(id);
	}

	public UserSubscription getUserSubscriptionById(Long userSubscriptionId) {
		return userSubscriptionRepository.findById(userSubscriptionId).orElse(null);
	}

	public List<UserSubscription> getAllUserSubscriptions() {
		return userSubscriptionRepository.findAll();
	}

	public UserSubscription findById(Long userSubscriptionId) {
		return userSubscriptionRepository.findById(userSubscriptionId)
				.orElseThrow(() -> new RuntimeException("Subscription not found"));
	}

}
