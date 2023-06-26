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

    /**
     * Creates a new instance of the Validator class with the specified user.
     *
     * @param user The User object associated with the verification code.
     */
    public Validator(User user) {
        this.user = user;
        this.verificationCodesManager = new VerificationCodesManager();
        getVerificationCode();
        System.out.println("=======================================================0\nEMAIL: " + user.getEmail() +
                "\nCODE: " + code + "\n=======================================================0\n");
    }

    /**
     * Obtains the verification code for the associated user.
     * Throws a NullPointerException if the User object is null.
     * Throws a RuntimeException if an exception occurs during the process.
     */
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

    /**
     * Retrieves the verification code for the associated user.
     * If the verification code is null, a new code is added and retrieved recursively.
     */
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

    /**
     * Returns the verification code associated with the Validator.
     *
     * @return The verification code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the User object associated with the Validator.
     *
     * @return The User object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the ID of the verification code associated with the Validator.
     *
     * @return The verification code ID.
     */
    public String getId() {
        return id;
    }
}