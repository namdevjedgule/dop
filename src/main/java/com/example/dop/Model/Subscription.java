package com.example.dop.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "sub_name_master_id", referencedColumnName = "id") // Ensure correct mapping
	private SubNameMaster subNameMaster;

	private String code;
	private Long price;
	private String project;
	@Column(name = "row_count")
	private String rowCount;
	private Long days;
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private Person createdBy;

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getRowCount() {
		return rowCount;
	}

	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public Person getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public SubNameMaster getSubNameMaster() {
		return subNameMaster;
	}

	public void setSubNameMaster(SubNameMaster subNameMaster) {
		this.subNameMaster = subNameMaster;
	}

	public void setCreatedBy(String name) {
		// TODO Auto-generated method stub

	}

}
