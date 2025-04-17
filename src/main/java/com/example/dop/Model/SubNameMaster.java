package com.example.dop.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SubNameMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscription_name_id")
	private Long subNameId;

	@Column(name = "Subcription_name", unique = true, nullable = false)
	private String subName;

	public Long getSubNameId() {
		return subNameId;
	}

	public void setSubNameId(Long subNameId) {
		this.subNameId = subNameId;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

}
