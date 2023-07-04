package com.isc.hermes.model.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.MainActivity;
import com.isc.hermes.model.User.TypeUser;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.User.UserRepositoryCreatorUsingDBRunnable;

/**
 * This class manages the transitions in the sign up process.
 */
public class SignUpTransitionHandler {

    /**
     * Loads user data into the database.
     * This method adds user details to the database, including email, full name,
     * username, user type, and the path to the user's image.
     * The method also retrieves and assigns the user's ID after adding them to the database.
     *
     * @throws RuntimeException If any other runtime exception occurs during the execution.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadUserDataInDB() {
        Thread threadAddUser = new Thread(new UserRepositoryCreatorUsingDBRunnable(
                UserRepository.getInstance().getUserContained()));
        threadAddUser.start();
    }

    /**
     * Launch's another activity, based on a role.
     *
     * @param packageContext the context, so the activity can be launched.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void transitionBasedOnRole(Context packageContext) {
        Intent intent;
        if (UserRepository.getInstance().getUserContained().getTypeUser().equals(TypeUser.ADMINISTRATOR.getTypeUser())) {
            intent = new Intent(packageContext, EmailVerificationActivity.class);
        } else {
            loadUserDataInDB();
            intent = new Intent(packageContext, MainActivity.class); }
        packageContext.startActivity(intent);
    }
}
