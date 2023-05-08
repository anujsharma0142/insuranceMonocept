package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class UserDetailsDto {
	
	private String email;

	private Long id;
	
	private String address;
	
	private String pincode;
	
	private String city;
	
	private String panCard;
	
	private String state;
	
	private String nomineeNo;
	
	private String nomineeRelation;
	
	private String nomineeName;
}
