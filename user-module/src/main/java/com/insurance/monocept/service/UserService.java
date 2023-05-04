package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.dto.InsuranceDto;
import com.insurance.monocept.dto.UserDetailsDto;
import com.insurance.monocept.dto.UserSignUpDto;

public interface UserService {

	ResponseEntity<?> signUp(UserSignUpDto signUpDto);

	ResponseEntity<?> login(UserSignUpDto signUpDto);

	ResponseEntity<?> getUserDetails();

	ResponseEntity<?> buyInsurance(InsuranceDto insurancedto);

	ResponseEntity<?> uploadDocuments(MultipartFile panCard, MultipartFile adhaarFront, MultipartFile adhaarBack, long insuranceId);

	ResponseEntity<?> addUserDetails(UserDetailsDto userDetailsDto);

}
