package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;
import com.isc.hermes.controller.authentication.IAuthentication;
import com.isc.hermes.model.User;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

import timber.log.Timber;


/**
 * This class is in charge of controlling the user's authentication activity.
 */
public class SignUpActivityView extends AppCompatActivity {

    private Map<Integer, IAuthentication> authenticationServices;
    private IAuthentication authenticator;

    /**
     * Method for creating the view and configuring it using the components xml.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
        initiateAuthenticationServices();
    }

    /**
     * This method Initializes the authentication services.
     * <p>
     * This method creates and initializes the available authentication services.
     * It uses the AuthenticationServices enumeration to retrieve the possible values
     * and create the corresponding authentication objects.
     * </p>
     */
    private void initiateAuthenticationServices() {
        authenticationServices = new HashMap<>();
        for (AuthenticationServices value : AuthenticationServices.values()) {
            authenticationServices.put(value.getID(), AuthenticationFactory.createAuthentication(value));
            Objects.requireNonNull(authenticationServices.get(value.getID())).configureAccess(this);
        }
    }

    /**
     * This method is Called when the activity is starting or resuming.
     * <p>
     * Checks the authentication status of each authentication service to confirm if there is an active session.
     * </p>
     */
    @Override
    protected void onStart() {
        super.onStart();
        authenticationServices.forEach((key, authentication) -> {
            if (authentication.checkUserSignIn(this)) {
                Toast.makeText(SignUpActivityView.this, "The user is already authenticated", Toast.LENGTH_SHORT).show();
                //The functionality for sign should be here
            }
        });
    }

    /**
     * This method allows the user to register the user
     *
     * @param view it contains the event info.
     */
    public void SignUp(View view) {
        if (authenticator != null) return;
        authenticator = authenticationServices.get(view.getId());
        if (authenticator == null) return;
        startActivityForResult(
                authenticator.signIn()
                , view.getId()
        );
    }

    private void sendUserBetweenActivities(User user) {
        Intent intent = new Intent(this, UserSignUpCompletionActivity.class);
        intent.putExtra("userObtained", user);
        startActivity(intent);
    }

    /**
     * This method controls the activity produced in the activity
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (authenticationServices.containsKey(requestCode))
                sendUserBetweenActivities(authenticator.getUserBySignInResult(data));
        } catch (ApiException e) {
            Toast.makeText(SignUpActivityView.this,"Wait a moment ",
                    Toast.LENGTH_SHORT).show();
            Timber.tag("LOG").e(e);
        }
    }

}