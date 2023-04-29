package com.foodstore.common.exception;

public class CuisineNotFoundException extends Exception {
	
	
	private static final long serialVersionUID = 1L;
	
	public CuisineNotFoundException(String message) {
		super(message);
	}
}
