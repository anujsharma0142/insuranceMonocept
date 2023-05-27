package com.insurance.monocept.exception;

public class EmployeeEmailIdAlreadyExists extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeEmailIdAlreadyExists(String message) {
		super(message);
	}

}
