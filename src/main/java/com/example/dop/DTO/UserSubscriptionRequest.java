package com.example.dop.DTO;

public class UserSubscriptionRequest {

	private String transactionId;
	private Long subscriptionId;
	private Integer projectAuthorized;
	private Integer fileRows;
	private String paymentBy;
	private String currency;
	private Double amount;
	private String paymentStatus;
	private String createdBy;
	private Long userId;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
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

	public String getPaymentBy() {
		return paymentBy;
	}

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
