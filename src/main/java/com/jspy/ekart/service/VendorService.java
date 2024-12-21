package com.jspy.ekart.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.repository.VendorRepository;

import jakarta.validation.Valid;

@Service
public class VendorService {

	@Autowired
	VendorRepository vendorRepository;

	public String vendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		modelMap.put("vendordto", vendordto);
		return "vendor-register.html";
	}

	public String registeration(@Valid Vendordto vendordto, BindingResult bindingResult) {
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
			vendorRepository.save(vendordto);
			// email Logic
			System.out.println(vendordto.getOtp());
			return "redirect:/vendor/otp/" + vendordto.getId();
		}
	}

	public String verifyOtp(int id, int otp) {
		Vendordto vendordto=vendorRepository.findById(id).orElseThrow();
		 if (vendordto.getOtp()==otp) {
			     vendordto.setVerified(true);
			     vendorRepository.save(vendordto);
			    return "redirect:/";
		} else { 
			return "redirect:/vendor/otp/"+ vendordto.getId();
		} 
	}
}
