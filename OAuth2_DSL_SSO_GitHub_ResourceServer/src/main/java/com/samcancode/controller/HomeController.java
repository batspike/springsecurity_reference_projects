package com.samcancode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "home.html";
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "hello.html";
	}
}
