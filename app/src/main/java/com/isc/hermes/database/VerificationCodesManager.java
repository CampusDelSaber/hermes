package com.isc.hermes.database;

import com.isc.hermes.model.Validator;
import com.isc.hermes.model.VerificationCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The VerificationCodesManager class is responsible for managing verification codes
 * in the application's database.
 */
public class VerificationCodesManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;
    private static String VERIFICATION_CODES_COLLECTION_NAME = "VerificationCodes";
    private VerificationCode verificationCodeObject;

    /**
     * Constructs a VerificationCodesManager object.
     */
    public VerificationCodesManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
        verificationCodeObject = VerificationCode.getVerificationCodeInstance();
    }

    /**
     * Adds a verification code for the specified user email.
     */
    public void addVerificationCode(String email) {
        apiHandler.postFutureCollections(VERIFICATION_CODES_COLLECTION_NAME, verificationCodeObject);
    }

    /**
     * Updates the validity status of a verification code.
     *
     * @param id      The ID of the verification code.
     * @param isValid The validity status to be set.
     */
    public void updateVerificationCode(String id, boolean isValid) {
        String params = VERIFICATION_CODES_COLLECTION_NAME + "/" + id;
        apiHandler.updateVerificationCodeValidity(params, isValid);
    }

    /**
     * Retrieves a specific verification code based on the email.
     *
     * @param email The email associated with the verification code.
     * @return A JSONArray containing the matching verification code(s).
     * @throws ExecutionException   If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public JSONArray getSpecificVerificationCode(String email) throws ExecutionException, InterruptedException {
        String params = VERIFICATION_CODES_COLLECTION_NAME + "/email/" + email;
        Future<String> future = apiHandler.getFutureCollectionString(params);
        String futureResponse = future.get();
        return apiResponseParser.getJSONArrayOnResult(futureResponse);
    }

    /**
     * Retrieves the last verification code for the specified email.
     *
     * @return The last valid verification code as a VerificationCode object.
     * @throws ExecutionException   If an execution error occurs.
     * @throws InterruptedException If the operation is interrupted.
     * @throws JSONException        If a JSON error occurs.
     */
    public VerificationCode getLastVerificationCode(String email) throws ExecutionException, InterruptedException, JSONException {
        JSONArray array = getSpecificVerificationCode(email);
        for (int index = 0; index < array.length(); index++) {
            JSONObject verificationCodesArray = array.getJSONObject(index);
            String id = verificationCodesArray.getString("_id");
            String userEmail = verificationCodesArray.getString("email");
            String verificationCode = verificationCodesArray.getString("verificationCode");
            boolean valid = verificationCodesArray.getBoolean("isValid");
            if (valid) {
                verificationCodeObject.setEmail(userEmail);
                verificationCodeObject.setVerificationCode(verificationCode);
                verificationCodeObject.setValid(true);
                verificationCodeObject.setId(id);
                index = array.length();
            }
        }
        return verificationCodeObject;
    }

    public void deleteVerificationCode(String idVerificationCode) {
        apiHandler.deleteFutureCollections(VERIFICATION_CODES_COLLECTION_NAME, idVerificationCode);
    }
}