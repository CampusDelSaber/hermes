package com.isc.hermes.controller.authentication;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.isc.hermes.R;
import com.isc.hermes.user.Payload;
import com.isc.hermes.user.Repository;

import java.util.Objects;

/**
 * This class is in charge of authentication by the google service
 */
public class GoogleAuthentication implements IAuthentication {
    private GoogleSignInClient googleSignInClient;
    private Repository userRepository;


    /**
     * This method sets up access with Google to request user data when registering or logging in.
     */
    public void configureAccess(Context context) {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();
        this.googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

        userRepository = Repository.getInstance();
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
     * Method called when the sign-in button is clicked.
     * <p>
     * This method generates an Intent for startActivityForResult()
     * that will close the current session of the user
     * </p>
     */
    public void signOut(Context context) {
        configureAccess(context);
        googleSignInClient.signOut().addOnCompleteListener(task -> Toast.makeText(context, "Your account was closed successfully",
                Toast.LENGTH_SHORT).show());
    }

    /**
     * Retrieves the service type for authentication.
     *
     * @return The service type for authentication (in this case, AuthenticationServices.GOOGLE).
     */
    @Override
    public AuthenticationServices getServiceType() {
        return AuthenticationServices.GOOGLE;
    }

    /**
     * Method called when the sign-in button is clicked.
     * <p>
     * This method generates an Intent for startActivityForResult()
     * which removes all user information within the app
     * </p>
     */
    public void revokeAccess() {
        googleSignInClient.revokeAccess();
    }

    /**
     * This method verifies if a user has already logged in with a google account to the application.
     */
    public boolean checkUserSignIn(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context) != null;
    }

    /**
     * Retrieves a User object based on a GoogleSignInAccount.
     *
     * @param account The GoogleSignInAccount used to create the User object.
     * @return The User object created from the GoogleSignInAccount.
     */
    private Payload getUserByAccount(GoogleSignInAccount account) {

        return new Payload(
                account.getEmail(),
                account.getId(),
                Objects.requireNonNull(account.getPhotoUrl()).toString(),
                account.getGivenName(),
                account.getFamilyName());
    }

    /**
     * Handles the sign-in result.
     *
     * @param data The completed sign-in task.
     * @return a user with its elements.
     */
    public int getUserBySignInResult(Intent data) throws ApiException {
        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account;
        account = completedTask.getResult(ApiException.class);
        return userRepository.put(getUserByAccount(account));
    }
}
