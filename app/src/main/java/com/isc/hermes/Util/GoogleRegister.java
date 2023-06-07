package com.isc.hermes.Util;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This class will help us to log in, log out and delete account data within our app.
 * This class uses google services to perform these tasks
 */
public class GoogleRegister {

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    /**
     * Constructor class which initialize variables
     */
    public GoogleRegister(Activity activity){
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
    }

    /**
     * This method helps us to close the session that is open within our app
     * It will show us a message in the console which verifies that the session was closed correctly
     */
    public void signOut(){
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("YOUR ACCOUNT WAS CLOSED SUCCESSFULLY");
            }
        });
    }

    /**
     * This method helps us to delete the data of our account
     * It will show us a message in the console which verifies that the session was closed correctly
     */
    public void revokeAccess(){
        googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("YOUR ACCOUNT WAS SUCCESSFULLY DELETED");
            }
        });
    }
}
