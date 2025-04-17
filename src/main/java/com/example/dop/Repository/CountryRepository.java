package com.example.dop.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	List<Country> findAllByOrderByCountryIdAsc();
}
