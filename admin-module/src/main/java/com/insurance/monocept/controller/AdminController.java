package com.insurance.monocept.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.insurance.monocept.dto.InsuranceSchemeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;
import com.insurance.monocept.dto.PremiumPaymentDetailsDto;
import com.insurance.monocept.service.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
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
	
	@PostMapping("/addInsuranceScheme")
	public ResponseEntity<?> addInsuranceScheme(@ModelAttribute InsuranceSchemeDto insuranceSchemedto){
		return adminService.addInsuranceScheme(insuranceSchemedto);
	}
	
	@PostMapping("/premiumPaymentDetails")
	public ResponseEntity<?> premiumPaymentDetails(@ModelAttribute PremiumPaymentDetailsDto premiumPaymentDetailsdto){
		return adminService.premiumPaymentDetails(premiumPaymentDetailsdto);
	}
	
	@GetMapping("/getUserDocuments")
	public ResponseEntity<?> getUserDocuments(){
		return adminService.getUserDocuments();
	}
	
	@PutMapping("/approvedDocuments/{documentId}")
	public ResponseEntity<?> approvedDocuments(@RequestParam boolean approved, @PathVariable("documentId") long documentId){
		return adminService.approvedDocuments(approved, documentId);
	}
	
	@PutMapping("/changePassword/{id}")
	public ResponseEntity<?> changePassword(@RequestParam("password") String password, @PathVariable("id") long id){
		return adminService.changePassword(password, id);
	}
	
	@PutMapping("/changePasswordForAdmin")
	public ResponseEntity<?> changePasswordForAdmin(@RequestParam("password") String password){
		return adminService.changePasswordForAdmin(password);
	}
	
	@GetMapping("/getInsuranceType")
	public ResponseEntity<?> getInsuranceType(){
		return adminService.getInsuranceType( );
	}
	
	@GetMapping("/getAllInsuranceScheme")
	public ResponseEntity<?> getAllInsuranceScheme(){
		return adminService.getAllInsuranceScheme( );
	}
	
	@PutMapping("/updateInsuranceType")
	public ResponseEntity<?> updateInsuranceType(@ModelAttribute InsuranceTypeDto insuranceTypeDto){
		return adminService.updateInsuranceType(insuranceTypeDto);
	}
	
	@PutMapping("/updateInsuranceScheme")
	public ResponseEntity<?> updateInsuranceScheme(@ModelAttribute InsuranceSchemeDto insuranceSchemeDto){
		return adminService.updateInsuranceScheme(insuranceSchemeDto);
	}
	
	@GetMapping("/getAllCustomers/{pageNo}")
	public ResponseEntity<?> getAllCustomers(@PathVariable Integer pageNo){
		return adminService.getAllCustomers( pageNo);
	}
	
	@GetMapping("/getUploadedDcuments/{pageNo}")
	public ResponseEntity<?> getUploadedDcumentts(@RequestParam("type") String type, @PathVariable Integer pageNo){
		return adminService.getUploadedDcumentts(type, pageNo);
	}
	
	@GetMapping("/sendMail/{email}")
	public ResponseEntity<?> sendMail(@PathVariable String email){
		return adminService.sendMail(email);
	}
	
	
	
	
	
}
