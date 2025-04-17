package com.example.dop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dop.DTO.SignupOptionsResponse;
import com.example.dop.Repository.AboutUsRepository;
import com.example.dop.Repository.CountryRepository;
import com.example.dop.Repository.DesignationRepository;

@RestController
public class SignupController {

	@Autowired
	private DesignationRepository designationRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private AboutUsRepository aboutUsRepo;

	@GetMapping("/signupOptions")
	public ResponseEntity<SignupOptionsResponse> getSignupOptions() {
		SignupOptionsResponse response = new SignupOptionsResponse();
		response.setDesignations(designationRepo.findAllByOrderByDesignationIdAsc());
		response.setCountries(countryRepo.findAllByOrderByCountryIdAsc());
		response.setAboutUs(aboutUsRepo.findAllByOrderByIdAsc());
		return ResponseEntity.ok(response);
	}
}
