package com.isc.hermes.model;

import com.isc.hermes.database.VerificationCodesManager;
import com.isc.hermes.model.User.UserRepository;

/**
 * The Validator class is responsible for validating a verification code entered by the user.
 */
public class Validator {
    private String code;
    private String id;
    private final VerificationCodesManager verificationCodesManager;
    private VerificationCode verificationCode;

    /**
     * Creates a new Validator instance with the specified user.
     */
    public Validator() {
        this.verificationCodesManager = new VerificationCodesManager();
        getVerificationCode();
    }

    /**
     * Obtains the verification code for the associated user.
     *
     * @throws RuntimeException if an error occurs while obtaining the verification code.
     */
    public void obtainVerificationCode() {
        try {
            if (UserRepository.getInstance().getUserContained() != null)
                verificationCode = verificationCodesManager.getLastVerificationCode(UserRepository.getInstance().getUserContained().getEmail());
            else throw new NullPointerException("User object is null");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves the verification code for the associated user and sets it as the current code.
     * If no verification code exists, a new one will be generated.
     */
    public void getVerificationCode() {
        obtainVerificationCode();
        if (verificationCode.getVerificationCode() == null) {
            verificationCodesManager.addVerificationCode(UserRepository.getInstance().getUserContained().getEmail());
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
     * Returns the current verification code.
     *
     * @return The current verification code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the ID of the current verification code.
     *
     * @return The ID of the current verification code.
     */
    public String getId() {
        return id;
    }
}