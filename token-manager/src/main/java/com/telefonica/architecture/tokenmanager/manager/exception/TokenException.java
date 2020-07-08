package com.telefonica.architecture.tokenmanager.manager.exception;

public class TokenException extends Exception {

	public TokenException() {
	}

	public TokenException(String message) {
		super(message);
	}

	public TokenException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}