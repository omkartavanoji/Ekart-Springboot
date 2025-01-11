package com.jspy.ekart.service;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorService {
	@ExceptionHandler(NoResourceFoundException.class)
	public String handle() {
		return "404.html";
	}
}
