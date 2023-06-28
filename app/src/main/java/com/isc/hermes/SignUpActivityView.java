package com.isc.hermes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.ApiException;
import com.isc.hermes.controller.authentication.AuthenticationFactory;
import com.isc.hermes.controller.authentication.AuthenticationServices;
import com.isc.hermes.controller.authentication.IAuthentication;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User.User;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Utils.DataAccountOffline;
import com.isc.hermes.utils.offline.NetworkManager;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;


/**
 * This class is in charge of controlling the user's authentication activity.
 */
public class SignUpActivityView extends AppCompatActivity {
    private Map<AuthenticationServices, IAuthentication> authenticationServices;
    public static IAuthentication authenticator;
    private static final int REQUEST_CODE = 7;

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
            getUserDependingAbleNetwork(authentication);
        });
    }

    /**
     * Get an User Depending the app is connected to internet
     *
     * @param authentication The authentication object representing the signed-in user.
     */
    private void getUserDependingAbleNetwork(IAuthentication authentication){

        if (authentication.checkUserSignIn(this)) {

            authenticator = authentication;
            User user = null;
            if(NetworkManager.isOnline(this)){
                user = obtainUserUsingInternet(authentication);
            }else{
                user = DataAccountOffline.getInstance(this).loadDataLogged();
            }
            UserRepository.getInstance().setUserContained(user);
            changeActivityToMap();
        }
    }

    /**
     * Obtains the user object by retrieving data using internet connectivity.
     *
     * @param authentication The authentication object representing the signed-in user.
     * @return the user obtained
     */
    private User obtainUserUsingInternet(IAuthentication authentication){
        User user = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                AccountInfoManager manager = new AccountInfoManager();
                String id = manager.getIdByEmail(authentication.getUserSignIn().getEmail());
                user =  manager.getUserById(id);
                DataAccountOffline.getInstance(this).saveDataLoggedAccount(user);
            } catch (ExecutionException | InterruptedException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    /**
     * This method change the activity to the activity map.
     */
    private void changeActivityToMap(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
     * Changes the activity depending on whether the user is registered or not.
     *
     * @param user The user object containing the user's information.
     * @throws ExecutionException If an error occurs while executing the asynchronous task.
     * @throws InterruptedException If the current thread is interrupted while waiting for the asynchronous task to complete.
     * @throws JSONException If an error occurs while parsing JSON data.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changeActivityDependingIsUserIsRegistered(User user) throws ExecutionException, InterruptedException, JSONException {
        AccountInfoManager accountInfoManager = new AccountInfoManager();
        UserRepository.getInstance().setUserContained(user);
        DataAccountOffline.getInstance(this).saveDataLoggedAccount(user);
        Intent intent;

        if (accountInfoManager.verifyIfAccountIsRegistered(user.getEmail())) {
            intent = new Intent(this, MainActivity.class);
            UserRepository.getInstance().getUserContained().setId(accountInfoManager.getIdByEmail(user.getEmail()));
        } else intent = new Intent(this, UserSignUpCompletionActivity.class);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            changeActivityDependingIsUserIsRegistered(authenticator.getUserBySignInResult(data));
        } catch (ExecutionException | InterruptedException | JSONException | ApiException e) {
            Toast.makeText(SignUpActivityView.this,"Wait a moment ", Toast.LENGTH_SHORT).show();
            Timber.tag("LOG").e(e);
        }
    }

}