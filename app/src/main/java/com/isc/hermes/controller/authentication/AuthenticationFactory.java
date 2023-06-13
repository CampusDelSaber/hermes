package com.isc.hermes.controller.authentication;
/**
 * Factory class for creating authentication objects based on the authentication service ID.
 * <p>
 * This factory class provides a method to create authentication objects based on the provided
 * authentication service ID. It uses a switch statement to determine the specific authentication
 * implementation to create.
 * </p>
 */
public class AuthenticationFactory {
    /**
     * This method creates an authentication object based on the authentication service ID.
     *
     * @param authenticationId The authentication service ID.
     * @return The created authentication object.
     */
    public static IAuthentication createAuthentication(AuthenticationServices authenticationId) {
        return switch (authenticationId) {
            case GOOGLE -> new GoogleAuthentication();
        };
    }
}
