package com.isc.hermes.database;

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

    public void addVerificationCode(String verificationCode, String userEmail) {
    }
}
