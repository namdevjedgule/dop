package com.example.dop.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dop.Model.Subscription;
import com.example.dop.Model.Template;
import com.example.dop.Repository.TemplateRepo;

import jakarta.annotation.Resource;

@Service
public class TemplateService {

	 private static final String UPLOAD_DIR = "src/main/resources/static/templates/";
	 
	@Autowired
	TemplateRepo templateRepo;
	
	public void saveFile(Template c1) 
	{
		templateRepo.save(c1);
		
	}
	public Template saveFile(MultipartFile file) throws IOException 
	{
		
	    if (!file.isEmpty()) {
	        String originalFileName = file.getOriginalFilename();

	       
	        if (templateRepo.findByTemplateName(originalFileName).isPresent()) {
	            throw new IOException("File with the same name already exists!");
	        }

	       
	        File directory = new File(UPLOAD_DIR);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        Path filePath = Paths.get(UPLOAD_DIR + originalFileName);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        
	        Template template = new Template();
	        template.setTemplateName(originalFileName);
	        template.setFilePath(filePath.toString());  
	        template.setCreatedOn(new java.sql.Date(System.currentTimeMillis()));
	        

	        return templateRepo.save(template);
	    }
	    return null;
	}


	   
	
	public Page<Template> getPaginatedTemplate(int page, int pageSize, String keyword, String statusFilter) {
		 Pageable pageable = PageRequest.of(page, pageSize);

         return templateRepo.findAll(pageable);
	}
	public List<Template> searchSubByKeyword(String keyword) {
		 return templateRepo.findByTemplateNameContainingIgnoreCase(keyword);
	}
	public List<Template> getTemplates() {
		
		return templateRepo.findAll();
	}
	
	public void deleteTemplatesByIds(List<Long> templateIds) {
	    templateRepo.deleteAllById(templateIds);
	}
	
	 public boolean fileExists(String fileName) {
	        return templateRepo.findByTemplateName(fileName).isPresent();
	    }
	 
	public void deleteTemplate(Long id) {
        templateRepo.deleteById(id);
    }
	
	
	public Optional<Template> getFileById(Long id) {
        return templateRepo.findById(id);
    }

	private final Path fileStorageLocation = Paths.get("F:/agri_project/HOMESTEADERINDIA-Spring-Boot-Java-Project-master/dop-main/dop-main/src/main/resources/static/templates/").toAbsolutePath().normalize();

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            System.out.println("🔍 Checking file at path: " + filePath);

            Resource resource = (Resource) new UrlResource(filePath.toUri());
            if (((File) resource).exists() && ((AbstractFileResolvingResource) resource).isReadable()) {
                return resource;
            } else {
                System.err.println("❌ File not found or not readable: " + filePath);
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            System.err.println("⚠️ Malformed URL: " + e.getMessage());
            throw new Exception("Invalid file path", e);
        }
    }
    public void updateTemplate(Template template) {
        templateRepo.save(template);
    }

	
    }
