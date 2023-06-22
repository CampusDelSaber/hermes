package com.isc.hermes.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.model.User;
import com.mapbox.geojson.Feature;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 The AccountInfoManager class is responsible for managing account information.
 */
public class AccountInfoManager {

    private ApiHandler apiHandler;
    private ApiResponseParser apiResponseParser;
    private ApiRequestHandler apiRequestHandler;

    private static String ACCOUNT_INFO_COLLECTION = "users";

    /**
     Constructs an instance of AccountInfoManager.
     */
    public AccountInfoManager() {
        apiHandler = ApiHandler.getInstance();
        apiRequestHandler = ApiRequestHandler.getInstance();
        apiResponseParser = ApiResponseParser.getInstance();
    }

    /**
     Adds a user to the account information collection.
     @param email the email of the user
     @param fullName the full name of the user
     @param userName the username of the user
     @param typeUser the type of the user
     @since API 26
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addUser(String email, String fullName, String userName, String typeUser) {
        User user = new User(fullName, email, userName, typeUser);
        apiHandler.postFutureCollections(ACCOUNT_INFO_COLLECTION, user);
    }

    /**
     Delete a user to the account information collection.
     @param idUser the id of the user
     @since API 26
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteUser(String idUser) {
        apiHandler.deleteFutureCollections(ACCOUNT_INFO_COLLECTION, idUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public User getUserById(String userId) throws ExecutionException, InterruptedException, JSONException {
        String apiUrl = ACCOUNT_INFO_COLLECTION + "/" + userId;
        Future<String> responseData = apiHandler.getFutureCollectionString(apiUrl);
        String futureRespons = responseData.get();
        JSONObject jsonObject = new JSONObject(futureRespons);
        return new User(
                jsonObject.getString("email"),
                jsonObject.getString("fullName"),
                jsonObject.getString("userName"),
                jsonObject.getString("typeUser"),
                jsonObject.getString("_id"));
    }
}
