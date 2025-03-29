package com.example.dop.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Admin;
import com.example.dop.Service.AdminService;

@Controller
public class AdminController
{
	@Autowired
	AdminService adminService;
	
	@GetMapping("/admin/add")
	public String Add()
	{
		return "AddAdmin";
	}
	
	@GetMapping("/admin/list")
    public String listAdmins(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "statusFilter", required = false) String statusFilter,
            Model model) {

        List<Admin> admins;

        if (keyword != null) keyword = keyword.trim();

        if (keyword != null && !keyword.isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
            admins = adminService.searchAdminsByStatus(keyword, statusFilter);
        } else if (keyword != null && !keyword.isEmpty()) {
            admins = adminService.searchAdmins(keyword);
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
           
            admins = adminService.getAdminsByStatus(statusFilter);
        } else {
            admins = adminService.getAdmins();
        }

        model.addAttribute("admins", admins);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", statusFilter); 

        return "AdminList"; 
    }
	
	 private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

	 @PostMapping("/saveAdmin")
	 public String saveAdmin(@ModelAttribute("a1") Admin a1, 
	                         @RequestParam("profileImage") MultipartFile file) {
	     if (!file.isEmpty()) {
	         try {
	             String uploadDir = new File("src/main/resources/static/uploads").getAbsolutePath();
	             File dir = new File(uploadDir);
	             if (!dir.exists()) {
	                 dir.mkdirs(); 
	             }

	             String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	             Path filePath = Paths.get(uploadDir, fileName);

	             Files.write(filePath, file.getBytes());

	             a1.setProfilePhoto("/uploads/" + fileName);
	         } catch (IOException e) {
	             e.printStackTrace();
	             return "redirect:/admin/add?error=file_upload_failed"; 
	         }
	     }

	    
	     a1.setStatus("Active");
	     adminService.savedata(a1);
	     return "redirect:/admin/add?success";
	 }

	    @PostMapping("/deleteSelected")
	    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
	                                 RedirectAttributes redirectAttributes) {
	        if (selectedIds != null && !selectedIds.isEmpty()) {
	            adminService.deleteAdmins(selectedIds);
	            redirectAttributes.addFlashAttribute("message", "Selected admins deleted successfully.");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "No admins selected for deletion.");
	        }
	        return "redirect:/admin/list";
	    }
	    
	    @GetMapping("/edit/{id}")
	    public String EditAdmin(@PathVariable("id") Long id, Model model) {
	        Admin a1 = adminService.editadmin(id);
	        
	        if (a1 == null) { // If admin is not found, redirect with an error message
	            return "redirect:/admin/list?error=AdminNotFound";
	        }

	        model.addAttribute("ap", a1);
	        return "EditAdmin"; 
	    }


	    @PostMapping("/UpdateAdmin")
	    public String updateAdmin(@ModelAttribute("ap") Admin ap) {
	        if (ap.getAid() == null) {
	            return "redirect:/ListAdmin?error=InvalidAdminId";
	        }

	        Admin existingAdmin = adminService.getAdminById(ap.getAid());

	        if (existingAdmin != null) 
	        { 
	        	String existingStatus = existingAdmin.getStatus(); 
	            
	            existingAdmin.setAfirstname(ap.getAfirstname());
	            existingAdmin.setAlastname(ap.getAlastname());
	            existingAdmin.setAemail(ap.getAemail());

	            if (ap.getApassword() != null && !ap.getApassword().trim().isEmpty()) {
	                existingAdmin.setApassword(ap.getApassword());
	            }

	            existingAdmin.setStatus(existingStatus);

	            adminService.savedata(existingAdmin);
	        }

	        return "redirect:/admin/list?success=AdminUpdated";
	    }

	    @GetMapping("/delete/{id}")
	    public String del(@PathVariable("id") Long id)
	    {
	    	adminService.deleteAdmin(id);
	    	return"redirect:/admin/list";
	    }
}