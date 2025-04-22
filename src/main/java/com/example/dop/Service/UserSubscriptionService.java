package com.example.dop.Service;

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

//	public UserSubscription saveSubscription(UserSubscriptionRequest request) {
//		UserSubscription subscription = new UserSubscription();
//
//		subscription.setTransactionId(request.getTransactionId());
//
//		Subscription sub = subRepo.findById(request.getSubscriptionId()).orElseThrow(
//				() -> new RuntimeException("Subscription not found with id: " + request.getSubscriptionId()));
//		subscription.setSubscription(sub);
//
//		User user = userRepository.findById(request.getUserId())
//				.orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
//		subscription.setUser(user);
//
//		subscription.setProjectAuthorized(request.getProjectAuthorized());
//		subscription.setFileRows(request.getFileRows());
//		subscription.setPaymentBy(request.getPaymentBy());
//		subscription.setCurrency(request.getCurrency());
//		subscription.setAmount(request.getAmount());
//		subscription.setPaymentStatus(request.getPaymentStatus());
//		subscription.setCreatedBy(request.getCreatedBy());
//		subscription.setCreatedDate(LocalDateTime.now());
//		subscription.setUpdatedBy(request.getCreatedBy());
//		subscription.setUpdatedDate(LocalDateTime.now());
//
//		return userSubscriptionRepository.save(subscription);
//	}

	public UserSubscription saveUserSubscription(UserSubscription userSubscription) {
		if (userSubscription.getUser() != null && userSubscription.getUser().getId() != null) {
			Long userId = userSubscription.getUser().getId();
			User existingUser = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
			userSubscription.setUser(existingUser);
		} else {
			throw new RuntimeException("User ID is missing while saving UserSubscription!");
		}

		if (userSubscription.getSubscription() != null
				&& userSubscription.getSubscription().getSubscriptionId() != null) {
			Long subId = userSubscription.getSubscription().getSubscriptionId();
			Subscription existingSub = subRepo.findById(subId)
					.orElseThrow(() -> new RuntimeException("Subscription not found with ID: " + subId));
			userSubscription.setSubscription(existingSub);
		} else {
			throw new RuntimeException("Subscription ID is missing while saving UserSubscription!");
		}

		return userSubscriptionRepository.save(userSubscription);
	}

//	public UserSubscription saveUserSubscription(UserSubscription userSubscription) {
//		if (userSubscription.getUser() != null && userSubscription.getUser().getId() != null) {
//			Long userId = userSubscription.getUser().getId();
//			User existingUser = userRepository.findById(userId)
//					.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
//			userSubscription.setUser(existingUser);
//		} else {
//			throw new IllegalArgumentException("User ID is missing while saving UserSubscription!");
//		}
//
//		if (userSubscription.getSubscription() != null
//				&& userSubscription.getSubscription().getSubscriptionId() != null) {
//			Long subId = userSubscription.getSubscription().getSubscriptionId();
//			Subscription existingSub = subRepo.findById(subId)
//					.orElseThrow(() -> new EntityNotFoundException("Subscription not found with ID: " + subId));
//			userSubscription.setSubscription(existingSub);
//		} else {
//			throw new IllegalArgumentException("Subscription ID is missing while saving UserSubscription!");
//		}
//
//		// Now save the UserSubscription and return it
//		return userSubscriptionRepository.save(userSubscription);
//	}

}
