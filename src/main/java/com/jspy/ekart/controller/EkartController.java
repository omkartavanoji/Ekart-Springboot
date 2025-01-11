package com.jspy.ekart.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jspy.ekart.controller.helper.CloudinaryImage;
import com.jspy.ekart.dto.Customerdto;
import com.jspy.ekart.dto.Items;
import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.dto.Vendordto;
import com.jspy.ekart.repository.CustomerRepository;
import com.jspy.ekart.repository.ItemRepository;
import com.jspy.ekart.repository.OrderRepository;
import com.jspy.ekart.repository.ProductRepository;
import com.jspy.ekart.service.CustomerService;
import com.jspy.ekart.service.VendorService;
import com.razorpay.Item;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.JSONObject;

@Controller
public class EkartController {
	@Autowired
	VendorService vendorService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CloudinaryImage cloudinaryImage;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Value("${admin.email}")
	String adminEmail;
	@Value("${admin.password}")
	String adminPassword;

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

	@GetMapping("/change/{id}")
	public String approveProducts(@PathVariable("id") int id, HttpSession session) {
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
	public String increase(@PathVariable int id, HttpSession session) {
		if (session.getAttribute("customerdto") != null) {
			Customerdto customerdto = (Customerdto) session.getAttribute("customerdto");
			Items item = itemRepository.findById(id).get();
			Productdto productdto = productRepository.findByProductNameLike(item.getProductName()).get(0);
			if (productdto.getProductStock() == 0) {
				session.setAttribute("failure", "Sorry! Product Out of Stock");
				return "redirect:/customer/cart";
			} else {
				item.setQuantity(item.getQuantity() + 1);
				item.setProductPrice(item.getProductPrice() + productdto.getProductPrice());
				itemRepository.save(item);
				productdto.setProductStock(productdto.getProductStock() - 1);
				productRepository.save(productdto);
				session.setAttribute("success", "Product Added to Cart");
				session.setAttribute("customerdto", customerRepository.findById(customerdto.getId()).get());
				return "redirect:/customer/cart";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	@GetMapping("/decrease/{id}")
	public String decrease(@PathVariable int id, HttpSession session) {
		if (session.getAttribute("customerdto") != null) {
			Customerdto customerdto = (Customerdto) session.getAttribute("customerdto");
			Items item = itemRepository.findById(id).get();
			Productdto productdto = productRepository.findByProductNameLike(item.getProductName()).get(0);
			if (item.getQuantity() > 1) {
				item.setQuantity(item.getQuantity() - 1);
				item.setProductPrice(item.getProductPrice() - productdto.getProductPrice());
				itemRepository.save(item);
				productdto.setProductStock(productdto.getProductStock() + 1);
				productRepository.save(productdto);
				session.setAttribute("success", "Product Quantity Reduced from Cart Success");
				session.setAttribute("customerdto", customerRepository.findById(customerdto.getId()).get());
				return "redirect:/customer/cart";
			} else {
				customerdto.getCart().getItems().remove(item);
				customerRepository.save(customerdto);
				session.setAttribute("failure", "Product Removed from Cart");
				session.setAttribute("customerdto", customerRepository.findById(customerdto.getId()).get());
				return "redirect:/customer/cart";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	@GetMapping("/payment")
	public String payment(HttpSession session, ModelMap modelMap) {
		Customerdto customerdto = (Customerdto) session.getAttribute("customerdto");
		if (session.getAttribute("customerdto") != null) {
			try {
				double amount = customerdto.getCart().getItems().stream().mapToDouble(i -> i.getProductPrice()).sum();
				RazorpayClient razorpayClient = new RazorpayClient("rzp_test_3hnFm8G3UzuKoD", "AbyiciRQXiHw1KFN0FmXQt5J");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("currency", "INR");
				jsonObject.put("amount", amount * 100);

				Order order = razorpayClient.orders.create(jsonObject);
				modelMap.put("key", "rzp_test_3hnFm8G3UzuKoD");
				modelMap.put("id", order.get("id"));
				modelMap.put("amount", amount * 100);
				modelMap.put("customerdto", customerdto);
				return "payment.html";
			} catch (RazorpayException e) {
				session.setAttribute("failure", "Invalid Session, First Login");
				return "redirect:/customer/login";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	@PostMapping("/success")
	public String paymentSucess(com.jspy.ekart.dto.Order order, HttpSession session) {
		if (session.getAttribute("customerdto") != null) {
			Customerdto customerdto = (Customerdto) session.getAttribute("customerdto");
			order.setCustomerdto(customerdto);
			order.setTotalPrice(customerdto.getCart().getItems().stream().mapToDouble(i -> i.getProductPrice()).sum());

			List<Items> items = customerdto.getCart().getItems();
			System.out.println(items.size());

			List<Items> orderItems = order.getItems();
			for (Items item : items) {
				Items item2 = new Items();
				item2.setProductCategory(item.getProductCategory());
				item2.setProductDescription(item.getProductDescription());
				item2.setProductImageLink(item.getProductImageLink());
				item2.setProductName(item.getProductName());
				item2.setProductPrice(item.getProductPrice());
				item2.setQuantity(item.getQuantity());
				orderItems.add(item2);
			}

			order.setItems(orderItems);
			orderRepository.save(order);

			customerdto.getCart().getItems().clear();
			customerRepository.save(customerdto);

			session.setAttribute("customerdto", customerRepository.findById(customerdto.getId()).get());
			session.setAttribute("success", "Order Placed Success");
			return "redirect:/customer/home";
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	@GetMapping("/customer/previousorders")
	public String viewOrders(HttpSession session, ModelMap modelMap) {
		if (session.getAttribute("customerdto") != null) {
			Customerdto customerdto = (Customerdto) session.getAttribute("customerdto");
			List<com.jspy.ekart.dto.Order> orders = orderRepository.findByCustomerdto(customerdto);
			if (orders.isEmpty()) {
				session.setAttribute("success", "No Orders Placed Yet");
				return "redirect:/customer/home";
			} else {
				modelMap.put("orders", orders);
				return "customer-view-orders.html";
			}

		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}
}
