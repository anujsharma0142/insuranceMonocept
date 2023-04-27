package com.insurance.monocept.enums;

public enum UserRoles {
	
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	AGENT("ROLE_AGENT"),
	EMPLOYEE("ROLE_EMPLOYEE");
	
	
	private String userRole;
	
	UserRoles(String userRole) {
		this.userRole = userRole;
	}
	
	public String getRole() {
		return this.userRole;
	}
}
