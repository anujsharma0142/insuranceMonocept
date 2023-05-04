package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.monocept.dto.AgentDto;
import com.insurance.monocept.service.AgentService;

@RequestMapping("/api/v1/user")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody AgentDto agentDto){
		return agentService.addCustomer(agentDto);
	}
	
	@GetMapping("/getUserDetails")
	public ResponseEntity<?> getUserDetails(){
		return agentService.getUserDetails();
	}
	
	
	
	
}
