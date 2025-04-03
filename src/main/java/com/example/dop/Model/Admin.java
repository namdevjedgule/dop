package com.example.dop.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("ADMIN")
public class Admin extends Person {

	@Transient
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

}
