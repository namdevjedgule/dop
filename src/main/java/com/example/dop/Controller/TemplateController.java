package com.example.dop.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Template;
import com.example.dop.Model.User;
import com.example.dop.Repository.TemplateRepo;
import com.example.dop.Service.AdminService;
import com.example.dop.Service.TemplateService;
import com.example.dop.Service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class TemplateController 
{
	@Autowired
	TemplateService templateService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TemplateRepo templetRepo;
	
	@GetMapping("/CreateTemplate")
	private String Ctemplate(HttpSession session, Model model)
	{
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", loggedInUser.getFirstName());
		
		return "CreateTemplate";
	}

	@PostMapping("/saveFile")
	public String saveFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpSession session) {
	    try {
	        templateService.saveFile(file);
	        redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
	    } catch (IOException e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage()); 
	    }
	   
	    return "redirect:/CreateTemplate";
	}

	 public boolean fileExists(String fileName) {
	        return templetRepo.findByTemplateName(fileName).isPresent();
	    }
	
	@GetMapping("/ViewTemplate")
	public String viewFile(@RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "statusFilter", required = false) String statusFilter,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        Model model, HttpSession session)
	{
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "redirect:/";
		}
		model.addAttribute("fname", loggedInUser.getFirstName());
List<Template> template;

	    
	    if (keyword != null) keyword = keyword.trim();

	    
	   if (keyword != null && !keyword.isEmpty()) {
		   template = templateService.searchSubByKeyword(keyword);
	    
	    } else {
	    	template = templateService.getTemplates();
	    }

	    model.addAttribute("template", template);
	    model.addAttribute("keyword", keyword);
	    
		 int pageSize = 5; 

		    Page<Template> TemplatePage = templateService.getPaginatedTemplate(page, pageSize, keyword, statusFilter);

		    model.addAttribute("Template", TemplatePage.getContent());
		    model.addAttribute("currentPage", page);
		    model.addAttribute("totalPages", TemplatePage.getTotalPages());
		return "ViewTemplate";
	}
	
	@PostMapping("/deleteSelectedFile")
	public String deleteSelectedFile(@RequestParam("selectedIds") List<Long> templateIds, RedirectAttributes redirectAttributes) {
	    if (templateIds != null && !templateIds.isEmpty()) {
	        templateService.deleteTemplatesByIds(templateIds);
	        redirectAttributes.addFlashAttribute("message", "Templates deleted successfully.");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "No templates selected.");
	    }
	    return "redirect:/ViewTemplate";
	}

	@GetMapping("/downloadMultiple")
	public ResponseEntity<byte[]> downloadMultipleFiles(@RequestParam List<Long> ids) {
	    try {
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

	        for (Long id : ids) {
	            Optional<Template> optionalTemplate = templateService.getFileById(id);
	            if (optionalTemplate.isPresent()) {
	                Template template = optionalTemplate.get();
	                Path filePath = Paths.get(template.getFilePath());

	                if (Files.exists(filePath)) {
	                    zipOutputStream.putNextEntry(new ZipEntry(template.getTemplateName()));
	                    Files.copy(filePath, zipOutputStream);
	                    zipOutputStream.closeEntry();
	                }
	            }
	        }

	        zipOutputStream.close();
	        byte[] zipBytes = byteArrayOutputStream.toByteArray();

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"selected_files.zip\"")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(zipBytes);

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().body(("Error while downloading: " + e.getMessage()).getBytes());
	    }
	}

	  @PostMapping("/deleteFile/{id}")
	    public String deleteTemplate(@PathVariable Long id) {
	        templateService.deleteTemplate(id);
	        return "redirect:/ViewTemplate";
	    }
	  
	  @GetMapping("/download/{id}")
	  public ResponseEntity<Object> downloadFile(@PathVariable Long id, HttpSession session) {
	      try {
	        
	          Template template = templateService.getFileById(id)
	                  .orElseThrow(() -> new RuntimeException("File not found in database."));

	          
	          String username = getCurrentUsername(session);
	          template.setUpdatedBy(username);
	          template.setUpdatedOn(new java.sql.Date(System.currentTimeMillis())); 
	          templateService.updateTemplate(template);

	         
	          Path filePath = Paths.get(template.getFilePath()).normalize();
	          if (!Files.exists(filePath)) {
	              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found at specified path.");
	          }

	    
	          byte[] content = Files.readAllBytes(filePath);

	         
	          return ResponseEntity.ok()
	                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + template.getTemplateName() + "\"")
	                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                  .body(content);

	      } catch (IOException e) {
	          return ResponseEntity.internalServerError().body("Error while downloading file: " + e.getMessage());
	      }
	  }
		
	  private String getCurrentUsername(HttpSession session) {
		  User loggedInUser = (User) session.getAttribute("loggedInUser");
		  return (loggedInUser != null) ? loggedInUser.getFirstName() + " " + loggedInUser.getLastName() : "UnknownUser";

	  }
}