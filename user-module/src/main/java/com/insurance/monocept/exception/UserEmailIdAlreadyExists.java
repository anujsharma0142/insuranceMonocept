package com.insurance.monocept.exception;

public class UserEmailIdAlreadyExists extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserEmailIdAlreadyExists(String message) {
		super(message);
	}

}
