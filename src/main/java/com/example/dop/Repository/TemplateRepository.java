package com.example.dop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
