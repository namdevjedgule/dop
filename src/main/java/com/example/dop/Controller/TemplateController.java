package com.example.dop.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Template;
import com.example.dop.Model.User;
import com.example.dop.Repository.TemplateRepo;
import com.example.dop.Service.TemplateService;
import com.example.dop.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/templates")
public class TemplateController {
	@Autowired
	TemplateService templateService;

	@Autowired
	UserService userService;

	@Autowired
	TemplateRepo templetRepo;

	@GetMapping("/add")
	public String showUploadTemplatePage(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());
		model.addAttribute("currentPage", "CreateTemplate");
		return "CreateTemplate";
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadTemplate(@RequestParam("file") MultipartFile file, HttpSession session) {
		String fileName = file.getOriginalFilename();

		try {
			if (fileExists(fileName)) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(Map.of("status", "error", "message", "File already exists in the database."));
			}

			User loggedInUser = (User) session.getAttribute("user");
			String createdBy = loggedInUser != null ? loggedInUser.getEmail() : "System";

			templateService.saveFile(file, createdBy);

			return ResponseEntity.ok(Map.of("status", "success", "message", "File uploaded successfully."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status", "error", "message", "Error uploading file: " + e.getMessage()));
		}
	}

	private boolean fileExists(String fileName) {
		return templetRepo.findByTemplateName(fileName).isPresent();
	}

	@GetMapping("/list")
	public String viewFile(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "statusFilter", required = false) String statusFilter,
			@RequestParam(value = "page", defaultValue = "0") int page, Model model, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null)
			return "redirect:/";

		model.addAttribute("fname", loggedInUser.getFirstName());
		model.addAttribute("email", loggedInUser.getEmail());
		model.addAttribute("picture", loggedInUser.getProfilePhoto());

		List<Template> template;

		if (keyword != null)
			keyword = keyword.trim();

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
	public String deleteSelectedFile(@RequestParam("selectedIds") List<Long> templateIds,
			RedirectAttributes redirectAttributes, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");
		if (loggedInUser == null) {
			redirectAttributes.addFlashAttribute("error", "Please log in to delete templates.");
			return "redirect:/login";
		}

		if (templateIds != null && !templateIds.isEmpty()) {
			try {
				templateService.deleteTemplatesByIds(templateIds, loggedInUser);
				redirectAttributes.addFlashAttribute("message", "Templates deleted successfully.");
			} catch (RuntimeException e) {
				redirectAttributes.addFlashAttribute("error", e.getMessage());
			}
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
					String fileName = optionalTemplate.get().getTemplateName();
					Path filePath = Paths.get("src/main/resources/static/templates/").resolve(fileName).normalize();

					if (Files.exists(filePath)) {
						zipOutputStream.putNextEntry(new ZipEntry(fileName));
						Files.copy(filePath, zipOutputStream);
						zipOutputStream.closeEntry();
					}
				}
			}

			zipOutputStream.close();
			byte[] zipBytes = byteArrayOutputStream.toByteArray();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"selected_files.zip\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(zipBytes);

		} catch (IOException e) {
			return ResponseEntity.internalServerError().body(("Error while downloading: " + e.getMessage()).getBytes());
		}
	}

	@PostMapping("/deleteFile/{id}")
	public ResponseEntity<?> deleteTemplate(@PathVariable Long id, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("user");

		if (loggedInUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "You must be logged in to perform this action."));
		}

		try {
			String result = templateService.deleteTemplate(id, loggedInUser);
			return ResponseEntity.ok(Collections.singletonMap("message", result));
		} catch (IllegalAccessException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An unexpected error occurred."));
		}
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Object> downloadFile(@PathVariable Long id, HttpSession session) {
		try {
			Optional<Template> optionalTemplate = templateService.getFileById(id);

			if (optionalTemplate.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Template template = optionalTemplate.get();
			String fileName = template.getTemplateName();
			Path filePath = Paths.get("src/main/resources/static/templates/").resolve(fileName).normalize();

			if (!Files.exists(filePath)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
			}

			User loggedInAdmin = (User) session.getAttribute("loggedInAdmin");
			User loggedInUser = (User) session.getAttribute("loggedInUser");

			String updatedBy = "System";
			if (loggedInAdmin != null) {
				updatedBy = loggedInAdmin.getEmail();
			} else if (loggedInUser != null) {
				updatedBy = loggedInUser.getEmail();
			}

			templateService.updateDownloadInfo(template, updatedBy);

			byte[] content = Files.readAllBytes(filePath);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(content);

		} catch (IOException e) {
			return ResponseEntity.internalServerError().body("Error while downloading: " + e.getMessage());
		}
	}

}