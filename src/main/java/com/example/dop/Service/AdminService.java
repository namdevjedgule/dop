package com.example.dop.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.Admin;
import com.example.dop.Model.Step;
import com.example.dop.Model.Subscription;
import com.example.dop.Repository.AdminRepo;

@Service
public class AdminService {
	@Autowired
	AdminRepo adminRepo;

	 private static final String UPLOAD_DIR = "uploads/admin";
	 
	public void savedata(Admin a1, MultipartFile file) throws IOException{
		if (!file.isEmpty()) {
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;

            File uploadDirectory = new File(UPLOAD_DIR);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir(); 
            }

            file.transferTo(Paths.get(filePath)); 

           
            a1.setProfilePhoto("/" + filePath);
        }
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

	public Page<Admin> getPaginatedSubscriptions(int page, int pageSize, String keyword, String statusFilter) {
		Pageable pageable = PageRequest.of(page, pageSize);

        return adminRepo.findAll(pageable);
	}

	public void toggleAdminStatus(Long id) {
        Admin admin = adminRepo.findById(id).orElse(null);
        if (admin != null) {
            admin.setActive(!admin.isActive()); // Toggle active/inactive status
            adminRepo.save(admin); // Save the updated status
        }
    }

}