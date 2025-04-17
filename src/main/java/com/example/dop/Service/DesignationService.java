package com.example.dop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Designation;
import com.example.dop.Repository.DesignationRepository;

@Service
public class DesignationService {
	@Autowired
	private DesignationRepository designationRepository;

	public List<Designation> getAllDesignations() {
		return designationRepository.findAllByOrderByDesignationIdAsc();
	}
}
