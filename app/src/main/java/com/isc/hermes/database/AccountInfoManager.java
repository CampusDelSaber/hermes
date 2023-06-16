package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.User;

public class AccountInfoManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;

    private static String ACCOUNT_INFO_COLLECTION = "users";

    public AccountInfoManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addUser(String email, String fullName, String userName, String typeUser) {
        User user = new User(fullName, email, userName, typeUser);
        apiHandler.postFutureCollections(ACCOUNT_INFO_COLLECTION, user);
    }

}
