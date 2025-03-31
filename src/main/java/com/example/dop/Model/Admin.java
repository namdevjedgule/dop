package com.example.dop.Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;


@Entity
public class Admin
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long aid;
	private String afirstname;
	private String alastname;
	private String aemail;
	private String apassword;
	private String status;
	private LocalDate created_on;
	 @Column(name = "profile_photo") 
	    private String profilePhoto;
	 private boolean active = true;
	 
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAfirstname() {
		return afirstname;
	}
	public void setAfirstname(String afirstname) {
		this.afirstname = afirstname;
	}
	public String getAlastname() {
		return alastname;
	}
	public void setAlastname(String alastname) {
		this.alastname = alastname;
	}
	public String getAemail() {
		return aemail;
	}
	public void setAemail(String aemail) {
		this.aemail = aemail;
	}
	public String getApassword() {
		return apassword;
	}
	public void setApassword(String apassword) {
		this.apassword = apassword;
	}
	public LocalDate getCreated_on() {
		return created_on;
	}
	public void setCreated_on(LocalDate created_on) {
		this.created_on = created_on;
	}
	
	

}

