package com.insurance.monocept.service;

import org.springframework.http.ResponseEntity;

import com.insurance.monocept.dto.AdminDto;
import com.insurance.monocept.dto.EmployeeDto;
import com.insurance.monocept.dto.InsuranceSchemeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;
import com.insurance.monocept.dto.PremiumPaymentDetailsDto;

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

	ResponseEntity<?> addInsuranceScheme(InsuranceSchemeDto insuranceSchemedto);

	ResponseEntity<?> premiumPaymentDetails(PremiumPaymentDetailsDto premiumPaymentDetailsdto);

	ResponseEntity<?> approvedDocuments(boolean approved, long documentId);

	ResponseEntity<?> getUserDocuments();

	ResponseEntity<?> changePassword(String password, long id);

	ResponseEntity<?> getInsuranceType();

	ResponseEntity<?> updateInsuranceType(InsuranceTypeDto insuranceTypeDto);

	ResponseEntity<?> getAllInsuranceScheme();

	ResponseEntity<?> updateInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto);

	ResponseEntity<?> getAllCustomers(Integer pageNo);

	ResponseEntity<?> changePasswordForAdmin(String password);

	ResponseEntity<?> getUploadedDcumentts(String type, Integer pageNo);

	ResponseEntity<?> sendMail(String email);

}
