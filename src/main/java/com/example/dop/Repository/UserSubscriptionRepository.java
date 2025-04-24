package com.example.dop.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

	boolean existsByUserAndSubscription(User user, Subscription subscription);

	UserSubscription findByUserId(Long id);

	Optional<UserSubscription> findById(Long userSubscriptionId);

	Optional<UserSubscription> findTopByOrderByUserSubscriptionIdDesc();

}
