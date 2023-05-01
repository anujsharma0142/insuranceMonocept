package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.EmployeeDto;

public interface EmployeeService {
	
	ResponseEntity<?> addAgent(EmployeeDto employeeDto);

	ResponseEntity<?> getAgent(Integer pageNo);

	ResponseEntity<?> updateAgent(EmployeeDto employeeDto);

}
