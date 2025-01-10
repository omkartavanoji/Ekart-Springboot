package com.jspy.ekart.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jspy.ekart.dto.Customerdto;
import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.repository.CustomerRepository;
import com.jspy.ekart.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Component
public class CustomerService {
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	public String customerRegistration(Customerdto customerdto, BindingResult bindingResult, HttpSession session) {
		if (!customerdto.getPassword().equals(customerdto.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "error.password",
					"Password and Confirm Password do not match");
		}
		if (customerRepository.existsByEmail(customerdto.getEmail())) {
			bindingResult.rejectValue("email", "error.email", "Email Already Exists");
		}
		if (customerRepository.existsByMobile(customerdto.getMobile())) {
			bindingResult.rejectValue("mobile", "error.mobile", "Mobile Number Already Exists");
		}

		if (bindingResult.hasErrors()) {
			return "customer-register.html";
		} else {
			int otp = new Random().nextInt(100000, 1000000);
			customerdto.setOtp(otp);
			customerRepository.save(customerdto);
			System.out.println(otp);
			// email logic
			session.setAttribute("success", "OTP SENT SUCCESFULLY TO " + customerdto.getEmail());
			return "redirect:/customer/otp/" + customerdto.getId();
		}
	}

	public String verifyOtp(int otp, int id, HttpSession session) {
		Customerdto customerdto = customerRepository.findById(id).orElseThrow();
		if (otp == customerdto.getOtp()) {
			customerdto.setVerified(true);
			customerRepository.save(customerdto);
			session.setAttribute("success", "Customer Registered  Successfully");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "Incorrect OTP Try again");
			return "redirect:/customer/otp/" + customerdto.getId();
		}
	}

	public String customerLogin(String email, String password, HttpSession session) {
		Customerdto customerdto = customerRepository.findByEmail(email);
		if (customerdto == null) {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/customer/login";
		} else {
			if (password.equals(customerdto.getPassword())) {
				if (customerdto.isVerified()) {
					session.setAttribute("customerdto", customerdto);
					session.setAttribute("success", "Customer Logged in Successfully");
					return "redirect:/customer/home";
				} else {
					int otp = new Random().nextInt(100000, 999999);
					customerdto.setOtp(otp);
					customerRepository.save(customerdto);
					System.out.println(otp);
					// email Logic
					session.setAttribute("failure",
							"OTP SENT SUCCESFULLY TO " + customerdto.getEmail() + " FIRST VERIFY EMAIL FOR LOGGING IN");
					return "redirect:/customer/otp/" + customerdto.getId();
				}
			} else {
				session.setAttribute("failure", "Incorrect Password");
				return "redirect:/customer/login";
			}
		}
	}

	public String loadCustomerViewProductsPage(HttpSession session, ModelMap modelMap) {
		if (session.getAttribute("customerdto") != null) {
			List<Productdto> productdto = productRepository.findByApprovedTrue();
			if (productdto.isEmpty()) {
				session.setAttribute("failure", "No Products Available");
				return "redirect:/customer/home";
			} else {
				modelMap.put("productdto", productdto);
				return "customer-view-products.html";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, Please Login First");
			return "redirect:/customer/login";
		}
	}

	public String customerSearch(String search, HttpSession session, ModelMap modelMap) {
		if (session.getAttribute("customerdto") != null) {
			if (search.isEmpty()) {
				modelMap.put("search", null);
				return "search.html";
			}
			String toSearch = "%" + search + "%";
			List<Productdto> list = productRepository.findByApprovedTrueAndProductNameLike(toSearch);
			List<Productdto> list1 = productRepository.findByApprovedTrueAndProductDescriptionLike(toSearch);
			List<Productdto> list2 = productRepository.findByApprovedTrueAndProductCategoryLike(toSearch);
			HashSet<Productdto> productdto = new HashSet<>();
			productdto.addAll(list);
			productdto.addAll(list1);
			productdto.addAll(list2);
			System.out.println(productdto);
			System.out.println(productdto.isEmpty());
			modelMap.put("productdto", productdto.isEmpty() ? null : productdto);
			modelMap.put("search", search);
			return "search.html";
		} else {
			session.setAttribute("failure", "Invalid Session, First login");
			return "redirect:/customer/login";
		}
	}
}