//package com.example.dop.Model;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.DiscriminatorColumn;
//import jakarta.persistence.DiscriminatorType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Inheritance;
//import jakarta.persistence.InheritanceType;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "user")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
//public abstract class Person {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column(name = "first_name", nullable = false)
//	private String firstName;
//
//	@Column(name = "last_name", nullable = false)
//	private String lastName;
//
//	@Column(unique = true, nullable = false)
//	private String email;
//
//	@Column(nullable = false)
//	private String password;
//
//	private String status;
//
//	private LocalDateTime memberSince;
//
//	private LocalDateTime lastLogin;
//
//	private String profilePhoto;
//
//	private boolean active = true;
//
//	@ManyToOne
//	@JoinColumn(name = "role_id", nullable = false)
//	private RoleMaster role;
//
//	private LocalDateTime createdOn = LocalDateTime.now();
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = (email != null) ? email.toLowerCase() : null;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public RoleMaster getRole() {
//		return role;
//	}
//
//	public void setRole(RoleMaster role) {
//		this.role = role;
//	}
//
//	public LocalDateTime getCreatedOn() {
//		return createdOn;
//	}
//
//	public void setCreatedOn(LocalDateTime createdOn) {
//		this.createdOn = createdOn;
//	}
//
//	public LocalDateTime getMemberSince() {
//		return memberSince;
//	}
//
//	public void setMemberSince(LocalDateTime memberSince) {
//		this.memberSince = memberSince;
//	}
//
//	public LocalDateTime getLastLogin() {
//		return lastLogin;
//	}
//
//	public void setLastLogin(LocalDateTime lastLogin) {
//		this.lastLogin = lastLogin;
//	}
//
//	public String getProfilePhoto() {
//		return profilePhoto;
//	}
//
//	public void setProfilePhoto(String profilePhoto) {
//		this.profilePhoto = profilePhoto;
//	}
//
//	public boolean isActive() {
//		return active;
//	}
//
//	public void setActive(boolean active) {
//		this.active = active;
//	}
//
//}
