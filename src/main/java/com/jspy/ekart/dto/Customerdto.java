package com.jspy.ekart.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Entity
@Data
public class Customerdto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Size(min = 5, max = 30, message = "ENTER NAME BETWEEN 5-30 CHARACTERS")
	private String name;
	@Email(message = "ENTER PROPER EMAIL")
	@NotEmpty(message = "IT IS A REQUIRED FIELD")
	private String email;
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "* Enter atleast 8 characters consisting of one uppercase, one lowercase, one number, one special character")
	private String password;
	@Transient
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "* Enter atleast 8 characters consisting of one uppercase, one lowercase, one number, one special character")
	private String confirmPassword;
	@DecimalMin(value = "6000000000")
	@DecimalMax(value = "9999999999")
	private String mobile;
	private int otp;
	private boolean verified;
}
