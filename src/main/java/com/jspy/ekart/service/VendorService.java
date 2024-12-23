package com.jspy.ekart.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jspy.ekart.controller.helper.EmailSender_OTP;
import com.jspy.ekart.controller.helper.PasswordAES;
import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.repository.VendorRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class VendorService {

	@Autowired
	VendorRepository vendorRepository;
	
	@Autowired
	EmailSender_OTP emailSender_OTP;

	public String vendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		modelMap.put("vendordto", vendordto);
		return "vendor-register.html";
	}

	public String registeration(@Valid Vendordto vendordto, BindingResult bindingResult, HttpSession session) {
		if (!vendordto.getPassword().equals(vendordto.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", " error.confirmPassword",
					"*Password and ConfirmPassword do not match");
		}

		if (vendorRepository.existsByEmail(vendordto.getEmail())) {
			bindingResult.rejectValue("email", "error.email", "email already exists");
		}

		if (vendorRepository.existsByMobile(vendordto.getMobile())) {
			bindingResult.rejectValue("mobile", "error.mobile", "Mobile No Already Exists");
		}

		if (bindingResult.hasErrors()) {
			return "vendor-register.html";
		} else {
			int otp = new Random().nextInt(100000, 1000000);
			vendordto.setOtp(otp);
			vendordto.setPassword(PasswordAES.encrypt(vendordto.getPassword()));
			vendorRepository.save(vendordto);
			//email logic
           //emailSender_OTP.sendEmail(vendordto);
			System.out.println(vendordto.getOtp());
			session.setAttribute("success", "OTP SENT SUCCESFULLY TO "+vendordto.getEmail());
			return "redirect:/vendor/otp/" + vendordto.getId();
		}
	}

	public String verifyOtp(int id, int otp,HttpSession session) {
		Vendordto vendordto=vendorRepository.findById(id).orElseThrow();
		 if (vendordto.getOtp()==otp) {
			     vendordto.setVerified(true);
			     vendorRepository.save(vendordto);
			     session.setAttribute("success", "Vendor Account created Successfully");
			    return "redirect:/";
		} else { 
			 session.setAttribute("failure", "PLEASE ENTER CORRECT OTP");
			return "redirect:/vendor/otp/"+ vendordto.getId();
		} 
	}
}
