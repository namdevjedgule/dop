package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.AboutUs;

public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {

	List<AboutUs> findAllByOrderByIdAsc();
}
