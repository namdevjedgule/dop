package com.example.dop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Model.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

	boolean existsByUserAndSubscription(User user, Subscription subscription);

}
