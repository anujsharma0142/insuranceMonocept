package com.insurance.monocept.dto;

import javax.persistence.ManyToOne;


import com.insurance.monocept.entity.InsuranceScheme;
import com.insurance.monocept.entity.User;

import lombok.Data;
@Data
public class InsuranceDto {
	
	private Long id;
	
	private String accNo;
	
	private String isActive;
	
	private Long insuranceScheme;
	
	

}
