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
import com.insurance.monocept.service.EmployeeService;

@RestController
@RequestMapping("/api/vi/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/addAgent")
	public ResponseEntity<?> addAgent(@RequestBody EmployeeDto employeeDto){
		return employeeService.addAgent(employeeDto);
	}
	
	@GetMapping("/getAgent/{pageNo}")
	public ResponseEntity<?> getAgent(@PathVariable Integer pageNo){
		return employeeService.getAgent(pageNo);
	}
	
	@PutMapping("/updateAgent")
	public ResponseEntity<?> updateAgent(@RequestBody EmployeeDto employeeDto){
		return employeeService.updateAgent(employeeDto);
	}
	
}
