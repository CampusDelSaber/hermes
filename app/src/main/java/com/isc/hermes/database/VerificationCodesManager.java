package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.User;
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

    /**
     * Constructs a VerificationCodesManager object.
     */
    public VerificationCodesManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    /**
     * Adds a verification code for the specified user email.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addVerificationCode(String id, String email) {
        VerificationCode verificationCode = new VerificationCode(id, email);
        apiHandler.postFutureCollections(VERIFICATION_CODES_COLLECTION_NAME, verificationCode);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public VerificationCode getLastVerificationCode(String email) throws ExecutionException, InterruptedException, JSONException {
        JSONArray array = getSpecificVerificationCode(email);
        VerificationCode verificationCodeObject = null;
        for (int index = 0; index < array.length(); index++) {
            JSONObject verificationCodesArray = array.getJSONObject(index);
            String id = verificationCodesArray.getString("_id");
            String userEmail = verificationCodesArray.getString("email");
            String verificationCode = verificationCodesArray.getString("verificationCode");
            boolean valid = verificationCodesArray.getBoolean("isValid");
            if (valid) {
                verificationCodeObject = new VerificationCode(id, userEmail);
                verificationCodeObject.setVerificationCode(verificationCode);
                verificationCodeObject.setValid(true);
                index = array.length();
            }
        }
        return verificationCodeObject;
    }
}