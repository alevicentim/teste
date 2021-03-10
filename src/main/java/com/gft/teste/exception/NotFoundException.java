package com.gft.teste.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5555377603069096415L;
	
	public NotFoundException(String message) {
		super(message);
	}

}
