package com.isc.hermes.model.exceptions;

/**
 * The CorruptedTokenException is thrown whenever a token is detected to be
 * corrupted or unable to cash.
 */
public class CorruptedTokenException extends RuntimeException{
    public CorruptedTokenException(String message) {
        super(message);
    }
}
