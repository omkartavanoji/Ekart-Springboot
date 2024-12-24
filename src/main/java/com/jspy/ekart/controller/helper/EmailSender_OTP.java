package com.jspy.ekart.controller.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jspy.ekart.dto.Vendordto;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender_OTP {

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void sendEmail(Vendordto vendordto) {
		String email = vendordto.getEmail();
		String name = vendordto.getName();
		int otp = vendordto.getOtp();

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("arrowdragon2531@gmail.com");
			helper.setTo(email);
			helper.setSubject("Otp for Email Verification");
			Context context = new Context();
			context.setVariable("name", name);
			context.setVariable("otp", otp);
			String text = templateEngine.process("otp-email.html", context);
			helper.setText(text, true);
			javaMailSender.send(message);
		} catch (Exception e) { 
			e.printStackTrace();
			System.out.println("There is some issue");
		} 

	}

}
