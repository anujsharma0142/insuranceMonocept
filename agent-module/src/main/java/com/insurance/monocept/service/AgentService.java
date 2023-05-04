package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.AgentDto;

public interface AgentService {

	ResponseEntity<?> addCustomer(AgentDto agentDto);
	
	ResponseEntity<?> getUserDetails();

	

}
