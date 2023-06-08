package com.isc.hermes.controller.authentication;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.isc.hermes.R;

/**
 * This class is in charge of authentication by the google service
 */
public class GoogleAuthentication implements IAuthentication {
    private GoogleSignInClient googleSignInClient;


    /**
     * This method sets up access with Google to request user data when registering or logging in.
     */
    public void configureAccess(Context context) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();
        this.googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    /**
     * Method called when the sign-in button is clicked.
     * <p>
     * This method generates an Intent for startActivityForResult()
     * that will ask the user to sign in with their account and
     * also ask permission to their resources.
     * </p>
     */
    public Intent signIn() {
        return this.googleSignInClient.getSignInIntent();
    }

    /**
     * This method verifies if a user has already logged in with a google account to the application.
     */
    public boolean checkUserSignIn(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    /**
     * Handles the sign-in result.
     *
     * @param data The completed sign-in task.
     */
    public void handleSignInResult(Intent data) throws ApiException {
        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account;
            account = completedTask.getResult(ApiException.class);
            // The verification IdToken will be do it by another task
            System.out.println(account.getEmail());
    }
}
