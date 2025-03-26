package com.example.dop.Model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "services")  // Optional: Change table name to avoid reserved keyword issues
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    private String sname;
    private String category;
    private String icon;
    private String altTitle;

    @Column(length = 1000)
    private String description; 

    private String status;
    private LocalDate createdOn;

    @Column(name = "profile_photo")  
    private String profilePhoto;

    // Getters and Setters
    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAltTitle() {
        return altTitle;
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
