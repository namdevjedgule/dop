package com.example.dop.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class ImageController {

	private static final String UPLOAD_DIR = "uploads/";

	@GetMapping("/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) throws Exception {
		Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
		Resource resource = new UrlResource(filePath.toUri());

		if (resource.exists()) {
			String contentType = Files.probeContentType(filePath);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
