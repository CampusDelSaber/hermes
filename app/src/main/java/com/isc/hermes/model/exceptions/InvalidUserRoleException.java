package com.isc.hermes.model.exceptions;

/**
 * The InvalidUserRoleException is thrown whenever an invalid user role is detected.
 */
public class InvalidUserRoleException extends RuntimeException{
    public InvalidUserRoleException(String message) {
        super(message);
    }
}
