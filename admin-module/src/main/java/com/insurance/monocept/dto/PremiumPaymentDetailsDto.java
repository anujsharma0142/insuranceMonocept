package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class PremiumPaymentDetailsDto {

	private Long id;
	
	private double amount;
	private String paymentDetails;
	private String status;
	
	private String type;
}
