package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.EmployeeDto;

public interface AdminService {

	ResponseEntity<?> addEmployee(EmployeeDto employeeDto);

	ResponseEntity<?> addAgent(EmployeeDto employeeDto);

	ResponseEntity<?> getAgent(Integer pageNo);

	ResponseEntity<?> getEmployee(Integer pageNo);

	ResponseEntity<?> updateEmployee(EmployeeDto employeeDto);

	ResponseEntity<?> updateAgent(EmployeeDto employeeDto);

}
