package com.example.dop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Template;

public interface TemplateRepo extends JpaRepository<Template, Long> {

	List<Template> findByTemplateNameContainingIgnoreCase(String keyword);

	Optional<Template> findByTemplateName(String fileName);

	List<Template> findByCreatedBy(String createdBy);

//	List<Template> findByCreatedBy(String email);

}
