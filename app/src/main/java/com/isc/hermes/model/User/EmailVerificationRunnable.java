package com.isc.hermes.model.User;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.isc.hermes.database.SendEmailManager;
import com.isc.hermes.model.Validator;
import com.isc.hermes.model.VerificationCode;

/**
 * This class represents a background task for email verification.
 * It sends an email with a verification code to an administrator's email address.
 */
public class EmailVerificationRunnable implements Runnable {

    /**
     * Runs the email verification task.
     * If the user is an administrator, an email with the verification code is sent.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        if (UserRepository.getInstance().getUserContained().getTypeUser().equals(
                TypeUser.ADMINISTRATOR.getTypeUser())) {
            new Validator();

            SendEmailManager sendEmailManager = new SendEmailManager();
            sendEmailManager.addEmail(UserRepository.getInstance().getUserContained().getEmail(),
                    VerificationCode.getVerificationCodeInstance().getVerificationCode());
        }
    }
}
