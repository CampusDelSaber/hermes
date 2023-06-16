package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.VerificationCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class VerificationCodesManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;
    private static String VERIFICATION_CODES_COLLECTION_NAME = "VerificationCodes";

    public VerificationCodesManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addVerificationCode(String userEmail) {
        VerificationCode verificationCode = new VerificationCode("0", userEmail);
        apiHandler.postFutureCollections(VERIFICATION_CODES_COLLECTION_NAME, verificationCode);
    }

    public void updateVerificationCode(String id, boolean isValid) {
        String params = VERIFICATION_CODES_COLLECTION_NAME + "/" + id;
        apiHandler.updateVerificationCodeValidity(params, isValid);
    }

    public JSONArray getSpecificVerificationCode(String email) throws ExecutionException, InterruptedException {
        String params = VERIFICATION_CODES_COLLECTION_NAME + "/email/" + email;
        Future<String> future = apiHandler.getFutureCollectionString(params);
        String futureResponse = future.get();
        return apiResponseParser.getJSONArrayOnResult(futureResponse);
    }

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
