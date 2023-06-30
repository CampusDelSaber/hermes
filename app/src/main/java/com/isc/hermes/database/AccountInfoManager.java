package com.isc.hermes.database;

import android.os.Build;
import androidx.annotation.RequiresApi;

import com.isc.hermes.model.User.User;
import com.isc.hermes.model.User.UserRepository;

import org.json.JSONArray;
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
    public void addUser(String email, String fullName, String userName, String typeUser, String pathImageUser) {
        User user = new User( email, fullName, userName, typeUser, pathImageUser);
        user.setRegistered(true);
        UserRepository.getInstance().getUserContained().setRegistered(true);
        apiHandler.postFutureCollections(ACCOUNT_INFO_COLLECTION, user);
    }

    /**
     * Retrieves a User object by the specified user ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object corresponding to the provided user ID
     * @throws ExecutionException  if an execution exception occurs while retrieving the user
     * @throws InterruptedException if the retrieval process is interrupted
     * @throws JSONException if there is an error in parsing the JSON response
     * @apiNote Requires API level {@link android.os.Build.VERSION_CODES#O} or higher
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public User getUserById(String userId) throws ExecutionException, InterruptedException, JSONException {
        String apiUrl = ACCOUNT_INFO_COLLECTION + "/" + userId;
        Future<String> responseData = apiHandler.getFutureCollectionString(apiUrl);
        JSONObject jsonObject = new JSONObject(responseData.get());
        return new User(jsonObject.getString("email"), jsonObject.getString("fullName"),
                jsonObject.getString("userName"), jsonObject.getString("typeUser"),
                jsonObject.getString("_id"), jsonObject.getString("pathImageUser"));
    }

    public boolean verifyIfAccountIsRegistered(String email) throws ExecutionException, InterruptedException, JSONException {
        boolean isRegistered = false;
        JSONArray arrayAccounts = getJSONArrayByEmail(email);
        if (arrayAccounts.length() != 0) {
            isRegistered = true;
        } return isRegistered;
    }

    private JSONArray getJSONArrayByEmail(String email) throws ExecutionException, InterruptedException, JSONException {
        String apiUrl = ACCOUNT_INFO_COLLECTION + "/email/" +  email;
        Future<String> future = apiHandler.getFutureCollectionString(apiUrl);
        return new JSONArray(future.get());
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

    /**
     * Retrieves a User object by the specified user ID.
     *
     * @param email the email of the user to retrieve
     * @return the User object corresponding to the provided user ID
     * @throws ExecutionException   if an execution exception occurs while retrieving the user
     * @throws InterruptedException if the retrieval process is interrupted
     * @throws JSONException        if there is an error in parsing the JSON response
     * @apiNote Requires API level {@link android.os.Build.VERSION_CODES#O} or higher
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getIdByEmail(String email) throws ExecutionException, InterruptedException, JSONException {
        JSONObject jsonObject = getJSONArrayByEmail(email).getJSONObject(0);
        return jsonObject.getString("_id");
    }

    /**
     * Updates the local user information using the email as a reference.
     */
    public void updateUserInformationLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                UserRepository.getInstance().setUserContained(getUserById(UserRepository.getInstance().getUserContained().getId()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     Edits a user's information.
     @param user the user will be used to update their information in db.
     @requiresApi(api = Build.VERSION_CODES.O) Requires API level O or higher
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void editUser(User user) {
        String apiUrl = ACCOUNT_INFO_COLLECTION + "/" + user.getId();
        apiHandler.putFutureCollection(apiUrl, user);
    }
}
