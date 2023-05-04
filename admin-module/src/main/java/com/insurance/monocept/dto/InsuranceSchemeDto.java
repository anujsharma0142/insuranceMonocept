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
	
	private double assuredAmount;
	
	private Long insuranceType; 
	
	private int duration;
	
	private int insuranceTax;
	
	private Long profitRatio;
	
	
}
