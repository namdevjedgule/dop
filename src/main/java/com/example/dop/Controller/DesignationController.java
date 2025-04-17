package com.example.dop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dop.Model.Designation;
import com.example.dop.Service.DesignationService;

@RestController
public class DesignationController {

	@Autowired
	private DesignationService designationService;

	@GetMapping("/designations")
	public List<Designation> getAllDesignations() {
		return designationService.getAllDesignations();
	}
}
