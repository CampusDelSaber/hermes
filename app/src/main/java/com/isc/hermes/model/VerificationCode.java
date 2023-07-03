package com.isc.hermes.model;

/**
 * The VerificationCode class represents a verification code associated with a user email.
 */
public class VerificationCode {

    private String id, email, verificationCode;
    private Boolean isValid;
    private static VerificationCode verificationCodeInstance;

    /**
     * Constructs a VerificationCode object with the specified ID and user email.
     */
    public VerificationCode() {
        this.verificationCode = null;
        this.isValid = true;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the validity status of the verification code.
     *
     * @param valid The validity status to set.
     */
    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static VerificationCode getVerificationCodeInstance() {
        if (verificationCodeInstance == null) {
            verificationCodeInstance = new VerificationCode();
        }
        return verificationCodeInstance;
    }
}