package com.insurance.monocept.dto;

import lombok.Data;

@Data
public class EmployeeDto {
	
	private Long id;

	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
	
	private boolean loginAllowed;
	
	private String mobileNo;
	
	private String qualification;
}
