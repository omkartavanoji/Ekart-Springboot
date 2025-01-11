package com.jspy.ekart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Autowired
	ProductRepository productRepository;

	@Value("${admin.email}")
	String adminEmail;
	@Value("${admin.password}")
	String adminPassword;

	public String verifyAdmin(String email, String password, HttpSession session) {
		if (email.equals(adminEmail)) {
			if (password.equals(adminPassword)) {
				session.setAttribute("admin", adminEmail);
				session.setAttribute("success", "Admin Logged in Successfully");
				return "redirect:/admin/home";
			} else {
				session.setAttribute("failure", "Invalid Password, Try again");
				return "redirect:/admin/login";
			}
		} else {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/admin/login";
		}
	}

	public String loadAdminProductsPage(HttpSession session, ModelMap modelMap) {
		if (session.getAttribute("admin") != null) {
			List<Productdto> productdto = productRepository.findAll();
			if (productdto != null) {
				modelMap.put("productdto", productdto);
				return "admin-view-products.html";
			} else {
				session.setAttribute("failure", "No Products found");
				return "redirect:/admin/home";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, Please Login First");
			return "redirect:/admin/login";
		}
	}

	public String adminApproveProducts(int id, HttpSession session) {
		if (session.getAttribute("admin") != null) {
			Productdto productdto = productRepository.findById(id).orElseThrow();
			if (productdto.isApproved()) {
				productdto.setApproved(false);
			} else {
				productdto.setApproved(true);
			}
			productRepository.save(productdto);
			session.setAttribute("success", "PRODUCT STATUS WITH NAME " + productdto.getProductName() + " HAS CHANGED");
			return "redirect:/admin/approve";
		} else {
			session.setAttribute("failure", "Invalid Session, Please Login First");
			return "redirect:/admin/login";
		}
	}

}
