package com.jspy.ekart.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Productdto {
	@Id
	@GeneratedValue(generator = "product_id")
	@SequenceGenerator(name = "product_id", initialValue = 111001, allocationSize = 1)
	private int id;
	private String productName;
	private double productPrice;
	private int productStock;
	private String productDescription;
	private String productCategory;
	private String productImageLink;
	private boolean approved;
	@Transient
	private MultipartFile productImage;
	@ManyToOne
	Vendordto vendordto;

}
