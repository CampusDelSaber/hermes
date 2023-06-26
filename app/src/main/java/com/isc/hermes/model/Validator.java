package com.isc.hermes.model;

import androidx.annotation.RequiresApi;

import android.os.Build;

import com.isc.hermes.database.VerificationCodesManager;

/**
 * The Validator class is responsible for validating a verification code entered by the user.
 */
public class Validator {
    private String code;
    private User user;
    private String id;
    private VerificationCodesManager verificationCodesManager;
    private VerificationCode verificationCode;

    public Validator(User user) {
        this.user = user;
        this.verificationCodesManager = new VerificationCodesManager();
        getVerificationCode();
        System.out.println("=======================================================0\nEMAIL: " + user.getEmail() +
                "\nCODE: " + code + "\n=======================================================0\n");
    }

    public void obtainVerificationCode() {
        try {
            if (user != null) {
                verificationCode = verificationCodesManager.getLastVerificationCode(user.getEmail());
            } else {
                throw new NullPointerException("User object is null");
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public void getVerificationCode() {
        obtainVerificationCode();
        if (verificationCode == null) {
            verificationCodesManager.addVerificationCode(user.getEmail());
            getVerificationCode();
        }
        code = verificationCode.getVerificationCode();
        id = verificationCode.getId();
    }

    /**
     * Checks if the entered code matches the actual code.
     *
     * @param userCode The code entered by the user.
     * @return True if the codes match, false otherwise.
     */
    public boolean isCorrect(String userCode) {
        return code.equals(userCode);
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }
}