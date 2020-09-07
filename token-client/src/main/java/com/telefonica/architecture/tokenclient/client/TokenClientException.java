package com.telefonica.architecture.tokenclient.client;

public class TokenClientException extends Exception {

    public TokenClientException() {
    }

    public TokenClientException(Throwable cause) {
        super(cause);
    }

    public TokenClientException(String message, Throwable cause) {
        super(message, cause);
    }
    
}