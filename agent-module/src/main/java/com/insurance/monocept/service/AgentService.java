package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.AgentDto;

public interface AgentService {

	ResponseEntity<?> addAgent(AgentDto agentDto);
	
	ResponseEntity<?> getUserDetails();

	

}
