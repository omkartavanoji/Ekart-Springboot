package com.jspy.ekart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jspy.ekart.dto.Vendordto;
import jakarta.validation.Valid;

@Controller
public class EkartController {
	@GetMapping
	public String loadHomepage() {
		return "home.html";
	}

	@GetMapping("/vendor/register")
	public String loadVendorRegistration(ModelMap modelMap  ,Vendordto vendordto) {
        modelMap.put("vendordto", vendordto);
		return "vendor-register.html";
	}

	@PostMapping("/vendor/register")
	public String vendorRegistration(@Valid Vendordto vendordto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) { 
			return "vendor-register.html";
		} else { 
			System.out.println(vendordto);
			return "redirect:https://www.youtube.com";
		}
	}

}
