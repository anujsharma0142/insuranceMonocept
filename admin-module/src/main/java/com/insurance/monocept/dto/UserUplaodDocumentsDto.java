package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class UserUplaodDocumentsDto {

	private Long id;
	
	private String panCard;
	
	private String adhaarFront;
	
	private String adhaarBack;
	
	private boolean approved;
	
	private String status;

	
}
