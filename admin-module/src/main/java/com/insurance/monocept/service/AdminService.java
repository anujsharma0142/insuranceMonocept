package com.insurance.monocept.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.AdminDto;
import com.insurance.monocept.dto.EmployeeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;

public interface AdminService {

	ResponseEntity<?> addEmployee(EmployeeDto employeeDto);

	ResponseEntity<?> addAgent(EmployeeDto employeeDto);

	ResponseEntity<?> getAgent(Integer pageNo);

	ResponseEntity<?> getEmployee(Integer pageNo);

	ResponseEntity<?> updateEmployee(EmployeeDto employeeDto);

	ResponseEntity<?> updateAgent(EmployeeDto employeeDto);

	ResponseEntity<?> addAdmin(AdminDto adminDto);

	ResponseEntity<?> addInsuranceType(InsuranceTypeDto insurancedto);

	ResponseEntity<byte[]> getImage(String fileName);

}
