package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.dto.AgentDto;
import com.insurance.monocept.dto.CustomerDto;
import com.insurance.monocept.dto.InsuranceDto;
import com.insurance.monocept.dto.UserDetailsDto;
import com.insurance.monocept.service.AgentService;

@RequestMapping("/api/v1/agent")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody AgentDto agentDto){
		return agentService.addCustomer(agentDto);
	}
	
	@GetMapping("/getAllAddedUser/{pageNo}")
	public ResponseEntity<?> getAllAddedUser(@PathVariable("pageNo") Integer pageNo){
		return agentService.getAllAddedUser(pageNo);
	}
	
	@GetMapping("/getUserDetails")
	public ResponseEntity<?> getUserDetails(@RequestParam String email){
		return agentService.getUserDetails(email);
	}
	
	@PutMapping("/updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody  CustomerDto customerDto){
		return agentService.updateCustomer(customerDto);
	}
	
	@PostMapping("/addInsurance")
	public ResponseEntity<?> addInsurance(@RequestBody InsuranceDto insuranceDto ){
		return agentService.addInsurance(insuranceDto);
	}
	
	@PostMapping("/addUserDetails")
	public ResponseEntity<?> addUserDetails(@RequestBody UserDetailsDto userDetailsDto ){
		return agentService.addUserDetails(userDetailsDto);
	}
	
	@PostMapping("/uploadDocuments/{insuranceId}")
	public ResponseEntity<?> uploadDocuments(@RequestPart("panCard") MultipartFile panCard, @RequestPart("adhaarFront") MultipartFile adhaarFront,
			@RequestPart("adhaarBack") MultipartFile adhaarBack, @PathVariable("insuranceId") long insuranceId){
		return agentService.uploadDocuments(panCard, adhaarFront, adhaarBack, insuranceId);
	}
	
	@GetMapping("/getAllInsuranceScheme")
	public ResponseEntity<?> getAllInsuranceScheme(){
		return agentService.getAllInsuranceScheme();
	}
	
	@GetMapping("/getInsuranceByUser/{userId}")
	public ResponseEntity<?> getInsuranceByUser(@PathVariable Long userId){
		return agentService.getInsuranceByUser(userId);
	}
	
	@GetMapping("/getInsuranceByUserEmail/{email}")
	public ResponseEntity<?> getInsuranceByUser(@PathVariable String email){
		return agentService.getInsuranceByUser(email);
	}
	
	
	
}
