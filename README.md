# 📦 Ekart WebApplication

Ekart WebApplication is a Spring Boot-based e-commerce platform designed for vendor and customer registration, product management, secure payment processing, and more. It provides an intuitive interface for both vendors and customers to interact with the platform. The application is powered by Thymeleaf for rendering dynamic HTML and uses Spring Boot for creating the REST APIs and backend services.

## 🛠 Features

- **Vendor Registration**: Vendors can register with their details, including email, mobile number, and product listings.
- **Customer Registration**: Customers can register to view products and make purchases.
- **OTP Verification**: Vendors receive an OTP for email verification during registration.
- **Password Encryption**: Secure password encryption using AES before storing in the database.
- **Product Management**: Vendors can add and manage their products.
- **Email OTP Sender**: Sends OTPs to vendors for registration confirmation.
- **Session Tracking**: Personalized shopping experience for users.
- **Responsive Design**: 📱 Optimized for mobile and desktop devices.
- **Payment Integration**: Integrated Razorpay for secure and easy payment processing.
- **User Authentication**: Secure user authentication with email verification using OTP.
- **Efficient Data Management**: Utilized Spring Data JPA for managing and storing data effectively.

## 🖥 Project Structure

### Controller Layer:
- `EkartController`: Handles the flow of the application, routing the requests.

### Service Layer:
- `VendorService`: Business logic for vendor-related functionalities.
- `CustomerService`: Business logic for customer-related functionalities.
- `PaymentService`: Handles payment processing using Razorpay API.

### Repository Layer:
- `VendorRepository`: Interacts with the database for vendor operations.
- `CustomerRepository`: Manages customer data operations.
- `ProductRepository`: Manages product data operations.

### DTO Layer:
- `VendorDto`: Data Transfer Object for Vendor details.
- `CustomerDto`: Data Transfer Object for Customer details.
- `ProductDto`: Data Transfer Object for Product details.
- `CartDto`: Storing customer Products For Cart details.

### Helper Layer:
- `CloudinaryImage`: For managing image uploads to the Cloud.
- `EmailSender_OTP`: Sends OTP for email verification.
- `MessageRemover`: Clears any unwanted messages.
- `PasswordAES`: Handles AES password encryption and decryption.

## ⚙️ Technologies Used

- **Spring Boot**: Backend framework to develop REST APIs and manage business logic.
- **Thymeleaf**: Template engine for rendering dynamic HTML pages.
- **Spring Data JPA**: For efficient data storage and management.
- **Razorpay**: Integrated for secure payment processing.
- **JPA (Java Persistence API)**: For database interaction.
- **MySQL**: Database for storing application data.
- **Cloudinary**: For handling image uploads and storage.
- **Bootstrap/Tailwind CSS**: For responsive and modern UI design.
