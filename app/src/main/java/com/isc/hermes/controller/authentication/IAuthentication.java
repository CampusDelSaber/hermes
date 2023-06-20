package com.isc.hermes.controller.authentication;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.isc.hermes.model.User;

/**
 * This is an Interface for authentication functionality.
 * <p>
 * This interface defines the methods required for authentication functionality. Implementing classes should provide
 * implementations for these methods to configure access.
 * </p>
 */
public interface IAuthentication {
    /**
     * This method configures access for the authentication process.
     *
     * @param context The activity context.
     */
    void configureAccess(Context context);

    /**
     *This method initiates the sign-in process.
     *
     * @return An Intent representing the sign-in process.
     */
    Intent signIn();

    /**
     * This method logs out the user session
     */
    void signOut(Context context);

    /**
     * Retrieves the service type for authentication.
     */
    AuthenticationServices getServiceType();

    /**
     * This method deletes the user information within the app
     */
    void revokeAccess(Context context);

    /**
     * This method checks the user's sign-in status.
     *
     * @param context The activity context.
     * @return True if the user is signed in, false otherwise.
     */
    boolean checkUserSignIn(Context context);

    /**
     * Handles the sign-in result.
     *
     * @param data The Intent data containing the sign-in result.
     * @return an account with user information.
     */
    User getUserBySignInResult(Intent data) throws ApiException;
}
