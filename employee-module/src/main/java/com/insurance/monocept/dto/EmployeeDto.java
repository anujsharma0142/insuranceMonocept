package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class EmployeeDto {
	
	private Long id;

	private String email;
	
	private String firstName;
	
	private String lasttName;
	
	private String password;
	
	private boolean loginAllowed;
	
	private String phoneNo;
	
	private String qalification;
}
