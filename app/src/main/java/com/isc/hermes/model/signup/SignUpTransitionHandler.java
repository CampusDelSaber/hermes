package com.isc.hermes.model.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.MainActivity;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.database.SendEmailManager;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Validator;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

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
        AccountInfoManager accountInfoManager = new AccountInfoManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            accountInfoManager.addUser(UserRepository.getInstance().getUserContained().getEmail(),
                    UserRepository.getInstance().getUserContained().getFullName(),
                    UserRepository.getInstance().getUserContained().getUserName(),
                    UserRepository.getInstance().getUserContained().getTypeUser(),
                    UserRepository.getInstance().getUserContained().getPathImageUser());
        try {
            UserRepository.getInstance().getUserContained().setId(accountInfoManager.getIdByEmail(UserRepository.getInstance().getUserContained().getEmail()));}
        catch (ExecutionException | InterruptedException | JSONException e) {
            throw new RuntimeException(e); }
    }

    /**
     * Launch's another activity, based on a role.
     *
     * @param packageContext the context, so the activity can be launched.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void transitionBasedOnRole(Context packageContext) {
        Intent intent;
        if (UserRepository.getInstance().getUserContained().getTypeUser().equals("Administrator")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendVerificationCode(UserRepository.getInstance().getUserContained().getTypeUser(),
                        UserRepository.getInstance().getUserContained().getEmail());
            intent = new Intent(packageContext, EmailVerificationActivity.class);
        } else {
            loadUserDataInDB();
            intent = new Intent(packageContext, MainActivity.class);
        } packageContext.startActivity(intent);
    }

    /**
     * Sends a verification code to the specified email address, based on the user roles,
     * only "Administrator".
     *
     * @param roles The user roles object containing the role information.
     * @param email The email address to which the verification code will be sent.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendVerificationCode(String roles, String email) {
        if (roles.equals("Administrator")) {
            Validator validator = new Validator(UserRepository.getInstance().getUserContained());
            validator.obtainVerificationCode();
            SendEmailManager sendEmailManager = new SendEmailManager();
            sendEmailManager.addEmail(email, validator.getCode());
        }
    }
}
