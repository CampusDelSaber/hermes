package com.isc.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
    private static final int REQUEST_CODE = 7;
    private Map<AuthenticationServices, IAuthentication> authenticationServices;
    public static IAuthentication authenticator;

    /**
     * Method for creating the view and configuring it using the components xml.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
        startAuthenticationServices();
        creationActionAboutUs();
    }

    /**
     *  It is the action of returning to about us, to get out of the sing In.
     *  You will find the button of "About Us", and by clicking it you can go back.
     */
    private void creationActionAboutUs(){
        TextView btn = findViewById(R.id.bttn_about_us);
        btn.setOnClickListener(v -> startActivity(
                new Intent(this,AboutUs.class)));
    }

    /**
     * This method Initializes the authentication services.
     * <p>
     * This method creates and initializes the available authentication services.
     * It uses the AuthenticationServices enumeration to retrieve the possible values
     * and create the corresponding authentication objects.
     * </p>
     */
    private void startAuthenticationServices() {
        authenticationServices = new HashMap<>();
        for (AuthenticationServices service : AuthenticationServices.values()) {
            authenticationServices.put(service, AuthenticationFactory.createAuthentication(service));
            Objects.requireNonNull(authenticationServices.get(service)).configureAccess(this);
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
                authenticator = authentication;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method allows the user to register the user
     *
     * @param view it contains the event info.
     */
    public void signUp(View view) {
        authenticator = authenticationServices.get(AuthenticationServices.valueOf((String) view.getTag()));
        if (authenticator == null) return;
        startActivityForResult( //TODO: Solve this is a deprecated method.
                authenticator.signIn()
                , REQUEST_CODE
        );
    }

    /**
     * Sends a User object to another activity using an Intent.
     *
     * @param user The User object to be sent to the other activity.
     */
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
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
                    sendUserBetweenActivities(authenticator.getUserBySignInResult(data));
            }
        } catch (ApiException e) {
            Toast.makeText(SignUpActivityView.this,"Wait a moment ",
                    Toast.LENGTH_SHORT).show();
            Timber.tag("LOG").e(e);
        }
    }

}