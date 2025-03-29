package com.example.dop.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "plans")
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private Long planId;

	@Column(name = "plan_title", nullable = false, unique = true)
	private String planName;

	@Column(name = "plan_price", nullable = false)
	private Double planPrice;

	@Column(name = "project_authorized", nullable = false)
	private Integer projectAuthorized;

	@Column(name = "file_rows", nullable = false)
	private Integer fileRows;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "plan_date")
	private LocalDateTime planDate;

	private String status;

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Double getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Double planPrice) {
		this.planPrice = planPrice;
	}

	public Integer getProjectAuthorized() {
		return projectAuthorized;
	}

	public void setProjectAuthorized(Integer projectAuthorized) {
		this.projectAuthorized = projectAuthorized;
	}

	public Integer getFileRows() {
		return fileRows;
	}

	public void setFileRows(Integer fileRows) {
		this.fileRows = fileRows;
	}

	public LocalDateTime getPlanDate() {
		return planDate;
	}

	public void setPlanDate(LocalDateTime planDate) {
		this.planDate = planDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
