package com.example.dop.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;
import com.example.dop.Repository.PlanRepository;
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
	private PlanRepository planRepository;

	@Autowired
	private SubRepo subRepo;

//	public UserSubscription saveUserSubscription(UserSubscription userSubscription) {
//		if (userSubscription.getUser() == null || userSubscription.getSubscription() == null) {
//			throw new RuntimeException("User or Subscription is missing in UserSubscription!");
//		}
//		return userSubscriptionRepository.save(userSubscription);
//	}

	public UserSubscription createUserSubscription(UserSubscription userSubscription, Long userId, Long subscriptionId,
			String createdBy) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		Subscription subscription = subRepo.findById(subscriptionId)
				.orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + subscriptionId));

		if (existsByUserAndSubscription(user, subscription)) {
			throw new RuntimeException("Subscription already exists for this user!");
		}

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

	public UserSubscription updateUserSubscription(UserSubscription userSubscription) {
		return userSubscriptionRepository.save(userSubscription);
	}

	public UserSubscription getUserSubscriptionById(Long id) {
		return userSubscriptionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User Subscription not found with ID: " + id));
	}

}
