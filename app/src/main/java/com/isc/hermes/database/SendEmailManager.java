package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.VerificationCode;

public class SendEmailManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;
    private static String SEND_EMAIL = "send-email";
    private VerificationCode verificationCode;


    /**
     * Constructs a VerificationCodesManager object.
     */
    public SendEmailManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    /**
     * Adds a verification code for the specified user email.
     *
     * @param userEmail The email of the user.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addEmail(String userEmail, String code) {
        //"send-email?to=garcia.villalobos.gabriela.4d@gmail.com&code=123"
        String url = SEND_EMAIL + "?to="+ userEmail + "&code=" + code;
        apiHandler.postFutureCollections(url);
    }
}
