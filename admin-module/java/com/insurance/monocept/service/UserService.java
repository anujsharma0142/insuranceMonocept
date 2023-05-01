package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.UserSignUpDto;

public interface UserService {

	ResponseEntity<?> signUp(UserSignUpDto signUpDto);

	ResponseEntity<?> login(UserSignUpDto signUpDto);

	ResponseEntity<?> getUserDetails();

}
