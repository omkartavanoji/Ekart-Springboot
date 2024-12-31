package com.jspy.ekart.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.jspy.ekart.dto.Customerdto;
import com.jspy.ekart.repository.CustomerRepository;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Component
public class CustomerService {
	@Autowired
	CustomerRepository customerRepository;

	public String customerRegistration(Customerdto customerdto, BindingResult bindingResult,HttpSession session) {
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
			session.setAttribute("success", "OTP SENT SUCCESFULLY TO "+customerdto.getEmail());
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
                  int otp=new Random().nextInt(100000,999999);
                  customerdto.setOtp(otp);
                  customerRepository.save(customerdto);
                  System.out.println(otp);
                  //email Logic
                  session.setAttribute("failure","OTP SENT SUCCESFULLY TO " + customerdto.getEmail() + " FIRST VERIFY EMAIL FOR LOGGING IN");
                  return "redirect:/customer/otp/"+customerdto.getId();
				}
			} else {
				session.setAttribute("failure", "Incorrect Password");
				return "redirect:/customer/login";
			}
		}
	}
}