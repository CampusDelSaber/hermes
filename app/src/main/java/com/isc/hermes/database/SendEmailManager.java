package com.isc.hermes.database;

import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * This class is responsible for creating a url to send a gmail.
 */
public class SendEmailManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;
    private final static String SEND_EMAIL = "send-email";
    private final static String TO = "?to=";
    private final static String CODE = "&code=";

    /**
     * Constructs a SendEmailManager object.
     */
    public SendEmailManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    /**
     * Add an email via a send request.
     *
     * @param userEmail the email address of the recipient.
     * @param code the code verification to be sent in the email.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addEmail(String userEmail, String code) {
        String url = SEND_EMAIL + TO+ userEmail + CODE + code;
        apiHandler.postFutureEmail(url);
    }
}
