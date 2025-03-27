package com.example.dop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Repository.UserRepository;

@Service
public class SubService
{
	@Autowired
	SubRepo subRepo;
	
	@Autowired
	UserRepository userRepository;

	public List<Subscription> searchsubByStatus(String keyword, String statusFilter)
	{
		return null;
	}

	public List<Subscription> searchSub(String keyword) {
        return subRepo.findByNameContainingIgnoreCase(keyword);
    }

	public List<Subscription> getSubByStatus(String statusFilter) {
		
		 return subRepo.findByStatus(statusFilter);
	}

	public List<Subscription> getSubcriptions() {
		return subRepo.findAll();
	}

	 public Subscription saveSubscription(Subscription subscription, User user) {
	        subscription.setCreatedBy(user);  
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

}
