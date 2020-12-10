package com.samcancode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// this is for testing the UserManagementConfig configuration is OK
@RestController
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "Welcome Home!";
	}
}
