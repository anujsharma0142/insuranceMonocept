package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.service.UserService;

@RequestMapping("/api/v1/user")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody UserSignUpDto signUpDto){
		return userService.signUp(signUpDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserSignUpDto signUpDto){
		return userService.login(signUpDto);
	}
}
