package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dop.Model.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	List<Designation> findAllByOrderByDesignationIdAsc();
}
