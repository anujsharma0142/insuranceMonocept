package com.insurance.monocept.dto;

import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.entity.InsuranceType;

import lombok.Data;

@Data
public class InsuranceSchemeDto  {
	
private Long id;
	
	private float commisionForInstallment;
	
	private float commisionForRegistration;
	
	private MultipartFile img;
	
	private Long insuranceType; 
	
	
}
