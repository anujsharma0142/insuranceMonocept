package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.monocept.dto.EmployeeDto;
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
	
}
