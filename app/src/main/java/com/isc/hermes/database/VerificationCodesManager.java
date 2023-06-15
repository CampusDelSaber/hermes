package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.VerificationCode;

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
        VerificationCode verificationCode = new VerificationCode(userEmail);
        apiHandler.postFutureCollections(VERIFICATION_CODES_COLLECTION_NAME, verificationCode);
    }
}
