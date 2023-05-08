package com.insurance.monocept.dto;

import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.entity.InsuranceType;

import lombok.Data;

@Data
public class InsuranceSchemeDto  {
	
	private long id;
	
	private float commissionForInstallment;
	
	private float commissionForRegistration;
	
	private MultipartFile img;
	
	private double assuredAmount;
	
	private Long insuranceType; 
	
	private int duration;
	
	private int insuranceTax;
	
	private String name;
	
	private Long profitRatio;
	
	private String status;
	
	
}
