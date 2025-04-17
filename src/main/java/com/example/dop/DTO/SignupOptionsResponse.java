package com.example.dop.DTO;

import java.util.List;

import com.example.dop.Model.AboutUs;
import com.example.dop.Model.Country;
import com.example.dop.Model.Designation;

public class SignupOptionsResponse {
	private List<Designation> designations;
	private List<Country> countries;
	private List<AboutUs> aboutUs;

	public List<Designation> getDesignations() {
		return designations;
	}

	public void setDesignations(List<Designation> designations) {
		this.designations = designations;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List<AboutUs> getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(List<AboutUs> aboutUs) {
		this.aboutUs = aboutUs;
	}

}
