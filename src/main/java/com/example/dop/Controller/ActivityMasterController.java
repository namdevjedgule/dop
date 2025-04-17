package com.example.dop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dop.Model.ActivityMaster;
import com.example.dop.Repository.ActivityMasterRepository;

@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "*")
public class ActivityMasterController {

	@Autowired
	private ActivityMasterRepository activityMasterRepository;

	@GetMapping("/all")
	public List<ActivityMaster> getAllActivities() {
		return activityMasterRepository.findAll();
	}
}
