package com.jspy.ekart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.service.VendorService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EkartController {
	@Autowired
	VendorService vendorService;

	@GetMapping  
	public String loadHomepage() {
		return "home.html";
	}

	@GetMapping("/vendor/register")
	public String loadVendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		return vendorService.vendorRegistration(modelMap, vendordto);
	}

	@PostMapping("/vendor/register")
	public String vendorRegistration(@Valid Vendordto vendordto, BindingResult bindingResult,HttpSession session) {
		return vendorService.registeration(vendordto, bindingResult,session);
	}
	
	@GetMapping("/vendor/otp/{id}")
	public String loadOtpPage(@PathVariable int id,ModelMap modelMap) {
		modelMap.put("id", id);
		return "vendor-otp.html";
	}
	
	@PostMapping("/vendor/otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp,HttpSession session) {
		  return vendorService.verifyOtp(id,otp,session);
	}
	
	
	@GetMapping("/vendor/login")
	public String loadVendorLoginPage() {
		return "vendor-login.html";
	}

}
