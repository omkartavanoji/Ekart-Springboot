package com.jspy.ekart.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity(name = "shopping_order")
@Data
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String razorpay_payment_id;
	private String razorpay_order_id;
	private double totalPrice;
	@CreationTimestamp
	private LocalDateTime orderDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<Items> items = new ArrayList<Items>();

	@ManyToOne
	Customerdto customerdto;

}
