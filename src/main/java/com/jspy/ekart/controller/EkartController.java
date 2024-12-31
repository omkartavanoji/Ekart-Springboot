package com.jspy.ekart.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jspy.ekart.dto.Customerdto;
import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.service.CustomerService;
import com.jspy.ekart.service.VendorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EkartController {
	@Autowired
	VendorService vendorService;

	@Autowired
	CustomerService customerService;

	@GetMapping
	public String loadHomepage() {
		return "home.html";
	}

	@GetMapping("/vendor/register")
	public String loadVendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		return vendorService.vendorRegistration(modelMap, vendordto);
	}

	@PostMapping("/vendor/register")
	public String vendorRegistration(@Valid Vendordto vendordto, BindingResult bindingResult, HttpSession session) {
		return vendorService.registeration(vendordto, bindingResult, session);
	}

	@GetMapping("/vendor/otp/{id}")
	public String loadOtpPage(@PathVariable int id, ModelMap modelMap) {
		modelMap.put("id", id);
		return "vendor-otp.html";
	}

	@PostMapping("/vendor/otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return vendorService.verifyOtp(id, otp, session);
	}

	@GetMapping("/vendor/login")
	public String loadVendorLoginPage() {
		return "vendor-login.html";
	}

	@PostMapping("/vendor/login")
	public String vendorLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return vendorService.vendorLogin(email, password, session);
	}

	@GetMapping("/vendor/home")
	public String loadVendorHomePage(HttpSession session) {
		if (session.getAttribute("vendordto") != null) {
			return "vendor-home.html";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	@GetMapping("/add/product")
	public String loadAddProductPage(HttpSession session) {
		if (session.getAttribute("vendordto") != null) {
			return "add-product.html";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	@PostMapping("/add/product")
	public String addProduct(Productdto productdto, HttpSession session) throws IOException {
		return vendorService.addProduct(productdto, session);
	}

	@GetMapping("/vendor/manageproduct")
	public String loadVendorProductsPage(HttpSession session, ModelMap map) {
		return vendorService.loadVendorProductsPage(session, map);
	}
	
	@GetMapping("/vendor/logout")
	public String vendorLogout(HttpSession session) {
		session.removeAttribute("vendordto");
		session.setAttribute("success", "Logged out successfully");
		return "redirect:/";
	}

	@GetMapping("/delete/{productId}")
	public String deleteVendorProduct(@PathVariable("productId") int id, HttpSession session, ModelMap map) {
		return vendorService.deleteVendorProduct(id, session, map);
	}

	@GetMapping("/customer/register")
	public String loadCustomerRegistrationPage(ModelMap modelMap, Customerdto customerdto) {
		modelMap.put("customerdto", customerdto);
		return "customer-register.html";
	}

	@PostMapping("/customer/register")
	public String customerRegistration(@Valid Customerdto customerdto, BindingResult bindingResult,HttpSession session) {
		return customerService.customerRegistration(customerdto, bindingResult,session);
	}

	@GetMapping("/customer/otp/{customerId}")
	public String loadCustomerOtpPage(@PathVariable("customerId") int id, ModelMap modelMap) {
		modelMap.put("id", id);
		return "customer-otp.html";
	}

	@PostMapping("/customer/otp")
	public String CustomerVerifyOtp(@RequestParam int otp, @RequestParam int id, HttpSession session) {
		return customerService.verifyOtp(otp, id, session);
	}

	@GetMapping("/customer/login")
	public String loadCustomerLoginPage() {
		return "customer-login.html";
	}

	@PostMapping("/customer/login")
	public String customerLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return customerService.customerLogin(email,password,session);
	}
	
	@GetMapping("/customer/home")
	public String loadCustomerHomePage(HttpSession session) {
		if(session.getAttribute("customerdto")!=null) {
			return "customer-home.html";
		}
		else {
			session.setAttribute("failure", "Invalid Session First Login ");
			return "redirect:/customer/login";
		}
	}
}
