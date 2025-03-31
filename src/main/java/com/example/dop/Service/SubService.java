package com.example.dop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.dop.Model.SubNameMaster;
import com.example.dop.Model.Subscription;
import com.example.dop.Model.User;
import com.example.dop.Repository.SubNameMasterRepository;
import com.example.dop.Repository.SubRepo;
import com.example.dop.Repository.UserRepository;

@Service
public class SubService
{
	@Autowired
	SubRepo subRepo;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SubNameMasterRepository subMasterRepo;

	public List<Subscription> searchsubByStatus(String keyword, String statusFilter)
	{
		return null;
	}

	public List<Subscription> searchSubByKeyword(String keyword) {
	    return subRepo.findBySubNameMaster_SubNameContainingIgnoreCase(keyword);
	}

	public List<Subscription> getSubByStatus(String status) {
	    return subRepo.findByStatus(status);
	}

//	public List<Subscription> searchSubByKeywordAndStatus(String keyword, String status) {
//	    return subRepo.findBySubNameMaster_SubNameContainingIgnoreCaseAndStatus(keyword, status);
//	}


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

	    public List<SubNameMaster> getAllSubNameMasters() {
	        return subMasterRepo.findAll();
	    }

	    public SubNameMaster findSubNameMasterById(Long subNameMasterId) {
	        return subMasterRepo.findById(subNameMasterId).orElse(null);
	    }

	    public Page<Subscription> getPaginatedSubscriptions(int page, int size, String keyword, String statusFilter) {
	        Pageable pageable = PageRequest.of(page, size);

	            return subRepo.findAll(pageable);
	    }
}


