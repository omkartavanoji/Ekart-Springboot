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
import com.jspy.ekart.service.AdminService;
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

	@Autowired
	AdminService adminService;

	@GetMapping
	public String loadHomepage() {
		return "home.html";
	}

	@GetMapping("/vendor/register")
	public String loadVendorRegistration(ModelMap modelMap, Vendordto vendordto) {
		return vendorService.loadVendorRegistration(modelMap, vendordto);
	}

	@PostMapping("/vendor/register")
	public String vendorRegistration(@Valid Vendordto vendordto, BindingResult bindingResult, HttpSession session) {
		return vendorService.vendorRegisteration(vendordto, bindingResult, session);
	}

	@GetMapping("/vendor/otp/{id}")
	public String loadVendorOtpPage(@PathVariable int id, ModelMap modelMap) {
		modelMap.put("id", id);
		return "vendor-otp.html";
	}

	@PostMapping("/vendor/otp")
	public String vendorVerifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return vendorService.vendorVerifyOtp(id, otp, session);
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
	public String loadVendorAddProductPage(HttpSession session) {
		if (session.getAttribute("vendordto") != null) {
			return "add-product.html";
		} else {
			session.setAttribute("failure", "INVALID SESSION, FIRST LOGIN");
			return "redirect:/vendor/login";
		}
	}

	@PostMapping("/add/product")
	public String vendorAddProduct(Productdto productdto, HttpSession session) throws IOException {
		return vendorService.vendorAddProduct(productdto, session);
	}

	@GetMapping("/vendor/manageproduct")
	public String loadVendorProductsPage(HttpSession session, ModelMap map) {
		return vendorService.loadVendorProductsPage(session, map);
	}

	@GetMapping("/delete/{productId}")
	public String vendorDeleteProduct(@PathVariable("productId") int id, HttpSession session) {
		return vendorService.vendorDeleteProduct(id, session);
	}

	@GetMapping("/vendor/logout")
	public String vendorLogout(HttpSession session) {
		session.removeAttribute("vendordto");
		session.setAttribute("success", "Logged out successfully");
		return "redirect:/";
	}

	@GetMapping("/customer/register")
	public String loadCustomerRegistrationPage(ModelMap modelMap, Customerdto customerdto) {
		modelMap.put("customerdto", customerdto);
		return "customer-register.html";
	}

	@PostMapping("/customer/register")
	public String customerRegistration(@Valid Customerdto customerdto, BindingResult bindingResult,
			HttpSession session) {
		return customerService.customerRegistration(customerdto, bindingResult, session);
	}

	@GetMapping("/customer/otp/{customerId}")
	public String loadCustomerOtpPage(@PathVariable("customerId") int id, ModelMap modelMap) {
		modelMap.put("id", id);
		return "customer-otp.html";
	}

	@PostMapping("/customer/otp")
	public String customerVerifyOtp(@RequestParam int otp, @RequestParam int id, HttpSession session) {
		return customerService.verifyOtp(otp, id, session);
	}

	@GetMapping("/customer/login")
	public String loadCustomerLoginPage() {
		return "customer-login.html";
	}

	@PostMapping("/customer/login")
	public String customerLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return customerService.customerLogin(email, password, session);
	}

	@GetMapping("/customer/home")
	public String loadCustomerHomePage(HttpSession session) {
		if (session.getAttribute("customerdto") != null) {
			return "customer-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session First Login ");
			return "redirect:/customer/login";
		}
	}

	@GetMapping("/customer/logout")
	public String customerLogout(HttpSession session) {
		session.removeAttribute("customerdto");
		session.setAttribute("success", "Customer Logged Out Successfully");
		return "redirect:/";
	}

	@GetMapping("/admin/login")
	public String adminLogin() {
		return "admin-login.html";
	}

	@PostMapping("/admin/login")
	public String verifyAdmin(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return adminService.verifyAdmin(email, password, session);
	}

	@GetMapping("/admin/home")
	public String loadAdminHomePage(HttpSession session) {
		if (session.getAttribute("admin") != null) {
			return "admin-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Please Login First");
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/admin/approve")
	public String loadAdminProductsPage(HttpSession session, ModelMap modelMap) {
		return adminService.loadAdminProductsPage(session, modelMap);
	}

	@GetMapping("/change/{id}")
	public String adminApproveProducts(@PathVariable("id") int id, HttpSession session) {
		return adminService.adminApproveProducts(id, session);
	}

	@GetMapping("/admin/logout")
	public String adminLogout(HttpSession session) {
		session.removeAttribute("admin");
		session.setAttribute("success", "ADMIN LOGGED OUT SUCCESSFULLY");
		return "redirect:/";
	}

	@GetMapping("/update/{product_id}")
	public String loadVendorUpdateProductPage(@PathVariable("product_id") int id, HttpSession session,
			ModelMap modelMap) {
		return vendorService.loadVendorUpdateProductPage(id, session, modelMap);
	}

	@PostMapping("/update/product")
	public String vendorUpdateProduct(Productdto productdto, HttpSession session) throws IOException {
		return vendorService.vendorUpdateProduct(productdto, session);
	}

	@GetMapping("/customer/product")
	public String loadCustomerViewProductsPage(HttpSession session, ModelMap modelMap) {
		return customerService.loadCustomerViewProductsPage(session, modelMap);
	}

	@GetMapping("/customer/search")
	public String customerSearchProducts(HttpSession session) {
		if (session.getAttribute("customerdto") != null) {
			return "search.html";
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	@PostMapping("/customer/search")
	public String customerSearch(@RequestParam String search, HttpSession session, ModelMap modelMap) {
		return customerService.customerSearch(search, session, modelMap);
	}

	@GetMapping("/customer/cart")
	public String loadCustomerCartPage(HttpSession session, ModelMap modelMap) {
		return customerService.loadCustomerCartPage(session, modelMap);
	}

	@GetMapping("/customer/add-to-cart/{id}")
	public String customerAddToCart(@PathVariable int id, HttpSession session) {
		return customerService.customerAddtoCart(id, session);
	}

	@GetMapping("/increase/{id}")
	public String productQuantityIncrease(@PathVariable int id, HttpSession session) {
		return customerService.productQuantityIncrease(id, session);
	}

	@GetMapping("/decrease/{id}")
	public String productQuantityDecrease(@PathVariable int id, HttpSession session) {
		return customerService.productQuantityDecrease(id, session);
	}

	@GetMapping("/payment")
	public String customerPayment(HttpSession session, ModelMap modelMap) {
		return customerService.customerPayment(session, modelMap);
	}

	@PostMapping("/success")
	public String customerPaymentSuccess(com.jspy.ekart.dto.Order order, HttpSession session) {
		return customerService.customerPaymentSuccess(order, session);
	}

	@GetMapping("/customer/previousorders")
	public String customerViewOrders(HttpSession session, ModelMap modelMap) {
		return customerService.customerViewOrders(session, modelMap);
	}
}
