package com.isc.hermes.model;

import androidx.annotation.RequiresApi;

import android.os.Build;

import com.isc.hermes.database.VerificationCodesManager;

/**
 * The Validator class is responsible for validating a verification code entered by the user.
 */
public class Validator {
    private String code;
    private String email;
    private Boolean valid;
    private String id;
    private VerificationCodesManager verificationCodesManager;
    private VerificationCode verificationCode;
    private static Validator validator;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Validator(String id, String email) {
        this.email = email;
        this.verificationCodesManager = new VerificationCodesManager();
        verificationCodesManager.addVerificationCode(id, email);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void obtainVerificationCode() {
        try {
            verificationCode = verificationCodesManager.getLastVerificationCode(email);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        code = verificationCode.getVerificationCode();
        valid = verificationCode.getValid();
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}