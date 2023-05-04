package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class AgentDto {
	
	private Long id;

	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
	
	private boolean loginAllowed;
	
	private String phoneNo;
	
	private String qualification;
	

}
