package com.jspy.ekart.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jspy.ekart.controller.helper.CloudinaryImage;
import com.jspy.ekart.controller.helper.EmailSender_OTP;
import com.jspy.ekart.controller.helper.PasswordAES;
import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.repository.ProductRepository;
import com.jspy.ekart.repository.VendorRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class VendorService {

	@Autowired
	VendorRepository vendorRepository;

	@Autowired
	CloudinaryImage cloudinaryImage;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	EmailSender_OTP emailSender_OTP;

	public String loadVendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		modelMap.put("vendordto", vendordto);
		return "vendor-register.html";
	}

	public String vendorRegisteration(@Valid Vendordto vendordto, BindingResult bindingResult, HttpSession session) {
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
			// email logic
			// emailSender_OTP.sendEmail(vendordto);
			System.out.println(vendordto.getOtp());
			session.setAttribute("success", "OTP SENT SUCCESFULLY TO " + vendordto.getEmail());
			return "redirect:/vendor/otp/" + vendordto.getId();
		}
	}

	public String vendorVerifyOtp(int id, int otp, HttpSession session) {
		Vendordto vendordto = vendorRepository.findById(id).orElseThrow();
		if (vendordto.getOtp() == otp) {
			vendordto.setVerified(true);
			vendorRepository.save(vendordto);
			session.setAttribute("success", "Vendor Account created Successfully");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "PLEASE ENTER CORRECT OTP");
			return "redirect:/vendor/otp/" + vendordto.getId();
		}
	}

	public String vendorLogin(String email, String password, HttpSession session) {
		Vendordto vendordto = vendorRepository.findByEmail(email);
		if (vendordto == null) {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/vendor/login";
		} else {
			if (PasswordAES.decrypt(vendordto.getPassword()).equals(password)) {
				if (vendordto.isVerified()) {
					session.setAttribute("vendordto", vendordto);
					session.setAttribute("success", "LOG IN SUCCESS");
					return "redirect:/vendor/home";
				} else {
					int otp = new Random().nextInt(100000, 1000000);
					vendordto.setOtp(otp);
					vendorRepository.save(vendordto);
					// email logic
					// emailSender_OTP.sendEmail(vendordto);
					System.out.println(vendordto.getOtp());
					session.setAttribute("success",
							"OTP SENT SUCCESFULLY TO " + vendordto.getEmail() + " FIRST VERIFY EMAIL FOR LOGGING IN");
					return "redirect:/vendor/otp/" + vendordto.getId();
				}
			} else {
				session.setAttribute("failure", "PLEASE ENTER CORRECT PASSWORD");
				return "redirect:/vendor/login";
			}
		}
	}

	public String vendorAddProduct(Productdto productdto, HttpSession session) throws IOException {
		if (session.getAttribute("vendordto") != null) {
			Vendordto vendordto = (Vendordto) session.getAttribute("vendordto");
			productdto.setVendordto(vendordto);
			productdto.setProductImageLink(cloudinaryImage.uploadFile(productdto.getProductImage()));
			productRepository.save(productdto);
			session.setAttribute("success", "PRODUCT ADDED SUCCESSFULLY");
			return "redirect:/vendor/home";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	public String loadVendorProductsPage(HttpSession session, ModelMap map) {
		if (session.getAttribute("vendordto") != null) {
			Vendordto vendordto = (Vendordto) session.getAttribute("vendordto");
			List<Productdto> product = productRepository.findByVendordto(vendordto);
			if (product.isEmpty()) {
				session.setAttribute("failure", "NO PRODUCTS FOUND");
				return "redirect:/vendor/home";
			} else {
				map.put("products", product);
				return "vendor-view-product.html";
			}
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	public String vendorDeleteProduct(int id, HttpSession session) {
		if (session.getAttribute("vendordto") != null) {
			productRepository.deleteById(id);
			session.setAttribute("success", "PRODUCT WITH ID " + id + " DELETED SUCCESSFULLY");
			return "redirect:/vendor/manageproduct";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	public String loadVendorUpdateProductPage(int id, HttpSession session, ModelMap modelMap) {
		if (session.getAttribute("vendordto") != null) {
			Productdto productdto = productRepository.findById(id).get();
			modelMap.put("productdto", productdto);
			return "vendor-update-product.html";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	public String vendorUpdateProduct(Productdto productdto, HttpSession session) throws IOException {
		if (session.getAttribute("vendordto") != null) {
			Vendordto vendordto = (Vendordto) session.getAttribute("vendordto");
			productdto.setProductImageLink(cloudinaryImage.uploadFile(productdto.getProductImage()));
			productdto.setVendordto(vendordto);
			productRepository.save(productdto);
			session.setAttribute("success", "Product Update With id " + productdto.getId());
			return "redirect:/vendor/manageproduct";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}
}
