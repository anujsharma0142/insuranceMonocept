package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class UserDetailsDto {

	private Long id;
	
	private String address;
	
	private String pincode;
	
	private String city;
	
	private String panCard;
	
	private String state;
}
