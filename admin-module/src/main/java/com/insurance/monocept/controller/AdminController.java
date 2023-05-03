package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.monocept.dto.AdminDto;
import com.insurance.monocept.dto.EmployeeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.service.AdminService;

@RestController
@RequestMapping("/api/vi/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/addEmployee")
	public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeDto){
		return adminService.addEmployee(employeeDto);
	}
	
	@PostMapping("/addAgent")
	public ResponseEntity<?> addAgent(@RequestBody EmployeeDto employeeDto){
		return adminService.addAgent(employeeDto);
	}
	
	@GetMapping("/getAgent/{pageNo}")
	public ResponseEntity<?> getAgent(@PathVariable Integer pageNo){
		return adminService.getAgent(pageNo);
	}
	
	@GetMapping("/getEmployee/{pageNo}")
	public ResponseEntity<?> getEmployee(@PathVariable Integer pageNo){
		return adminService.getEmployee( pageNo);
	}
	
	@PutMapping("/updateEmployee")
	public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDto employeeDto){
		return adminService.updateEmployee(employeeDto);
	}
	
	@PutMapping("/updateAgent")
	public ResponseEntity<?> updateAgent(@RequestBody EmployeeDto employeeDto){
		return adminService.updateAgent(employeeDto);
	}
	
	@PostMapping("/signupAdmin")
	public ResponseEntity<?> addAdmin(@RequestBody AdminDto adminDto){
		return adminService.addAdmin(adminDto);
	}
	
	@PostMapping("/addInsuranceType")
	public ResponseEntity<?> addInsuranceType(@ModelAttribute InsuranceTypeDto insurancedto){
		return adminService.addInsuranceType(insurancedto);
	}
	
	@GetMapping("/getImage/{fileName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String fileName){
		return adminService.getImage(fileName);
	}
	
	
	
	
	
}
