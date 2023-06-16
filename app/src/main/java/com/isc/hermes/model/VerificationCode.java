package com.isc.hermes.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.utils.CreateVerificationCode;

/**
 * The VerificationCode class represents a verification code associated with a user email.
 */
public class VerificationCode {

    private String id, email, verificationCode;
    private Boolean isValid;

    /**
     * Constructs a VerificationCode object with the specified ID and user email.
     *
     * @param id        The ID of the verification code.
     * @param userEmail The email associated with the verification code.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public VerificationCode(String id, String userEmail) {
        this.id = id;
        this.email = userEmail;
        this.verificationCode = new CreateVerificationCode().generateVerificationCode();
        this.isValid = true;
    }

    /**
     * Retrieves the ID of the verification code.
     *
     * @return The ID of the verification code.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the email associated with the verification code.
     *
     * @return The email associated with the verification code.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the verification code value.
     *
     * @return The verification code value.
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * Sets the verification code value.
     *
     * @param verificationCode The verification code value to set.
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     * Retrieves the validity status of the verification code.
     *
     * @return The validity status of the verification code.
     */
    public Boolean getValid() {
        return isValid;
    }

    /**
     * Sets the validity status of the verification code.
     *
     * @param valid The validity status to set.
     */
    public void setValid(Boolean valid) {
        isValid = valid;
    }
}