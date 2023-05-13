package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.dto.InsuranceDto;
import com.insurance.monocept.dto.UserDetailsDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.entity.PremiumPaymentDetails;
import com.insurance.monocept.repository.PremiumPaymentDetailsRepository;
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
	
	@GetMapping("/getUserDetails")
	public ResponseEntity<?> getUserDetails(){
		return userService.getUserDetails();
	}
	
	@PostMapping("/addUserDetails")
	public ResponseEntity<?> addUserDetails(@RequestBody UserDetailsDto userDetailsDto ){
		return userService.addUserDetails(userDetailsDto);
	}
	
	@PostMapping("/buyInsurance")
	public ResponseEntity<?> buyInsurance(@RequestBody InsuranceDto insurancedto ){
		return userService.buyInsurance(insurancedto);
	}
	
	@PostMapping("/uploadDocuments/{insuranceId}")
	public ResponseEntity<?> uploadDocuments(@RequestPart("panCard") MultipartFile panCard, @RequestPart("adhaarFront") MultipartFile adhaarFront,
			@RequestPart("adhaarBack") MultipartFile adhaarBack, @PathVariable("insuranceId") long insuranceId){
		return userService.uploadDocuments(panCard, adhaarFront, adhaarBack, insuranceId);
	}
	
	@GetMapping("/getPremiumPaymentDetails")
	public ResponseEntity<?> getPremiumPaymentDetails(){
	return userService.getPremiumPaymentDetails();
	}
	
	@PostMapping("/makePayment")
	public ResponseEntity<?> makeInitialPayment(@RequestParam long PaymentId){
		return userService.makeInitialPayment(PaymentId);
	}
	
	@GetMapping("/getPaymentSuccess")
	public ResponseEntity<?> makePaymentSuccess(){
		return userService.makePaymentSuccess();
	}
}
