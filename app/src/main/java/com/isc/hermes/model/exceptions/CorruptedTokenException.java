package com.isc.hermes.model.exceptions;

public class CorruptedTokenException extends RuntimeException{
    public CorruptedTokenException(String message) {
        super(message);
    }
}
