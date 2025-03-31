package com.example.dop.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dop.Model.Admin;
import com.example.dop.Repository.AdminRepo;

@Service
public class AdminService {
	@Autowired
	AdminRepo adminRepo;

	public void savedata(Admin a1) {
		a1.setCreated_on(LocalDate.now());
		adminRepo.save(a1);

	}

	public List<Admin> getAdmins() {

		return adminRepo.findAll();
	}

	public List<Admin> searchAdmins(String keyword) {
		return adminRepo.findByAfirstnameContainingIgnoreCaseOrAemailContainingIgnoreCase(keyword, keyword);
	}

	public List<Admin> searchAdminsByStatus(String keyword, String status) {
		return adminRepo.findByAfirstnameContainingIgnoreCaseOrAemailContainingIgnoreCaseAndStatus(keyword, keyword,
				status);
	}

	public List<Admin> getAdminsByStatus(String statusFilter) {
		return adminRepo.findByStatus(statusFilter);
	}

	public void deleteAdmins(List<Long> selectedIds) {

		adminRepo.deleteAllById(selectedIds);
	}

	public Admin editadmin(Long aid) {
		return adminRepo.findById(aid).orElse(null); // Return null if not found
	}

	public Admin getAdminById(Long aid) {

		return adminRepo.getById(aid);
	}

	public void deleteAdmin(Long id) {
		adminRepo.deleteById(id);

	}

	public long countAdmins() {
		return adminRepo.count();
	}

}