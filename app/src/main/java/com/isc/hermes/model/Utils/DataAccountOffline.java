package com.isc.hermes.model.Utils;

import android.animation.TypeEvaluator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.isc.hermes.model.User.User;
/**
 * This class is responsible for managing the offline storage of logged account data.
 */
public class DataAccountOffline {

    private Activity activity;
    private static final String EMAIL = "EMAIL", ID = "ID", FULL_NAME = "FULL_NAME", USER_NAME = "USER_NAME",
            PATH_IMG_US = "PATH_IMG_US", USER_TYPE = "USER_TYPE";
    private static DataAccountOffline dataAccountOffline;

    /**
     * This method private constructor to enforce singleton pattern.
     *
     * @param activity The activity instance.
     */
    private DataAccountOffline(Activity activity) {
        this.activity = activity;
    }

    /**
     * This method returns the instance of DataAccountOffline.
     *
     * @param activity The activity instance.
     * @return The DataAccountOffline instance.
     */
    public static DataAccountOffline getInstance(Activity activity) {
        if (dataAccountOffline == null) {
            dataAccountOffline = new DataAccountOffline(activity);
        }
        return dataAccountOffline;
    }

    /**
     * This method saves the data of the logged account when there is no network connectivity.
     *
     * @param user The user object representing the logged account.
     */
    public void saveDataLoggedAccount(User user) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, user.getEmail());
        editor.putString(ID, user.getId());
        editor.putString(FULL_NAME, user.getFullName());
        editor.putString(USER_NAME, user.getUserName());
        editor.putString(PATH_IMG_US, user.getPathImageUser());
        editor.putString(USER_TYPE, user.getTypeUser());

        editor.apply();
    }

    /**
     * This method retrieves the saved data of the logged account.
     *
     * @return The User object representing the logged account.
     */
    public User loadDataLogged() {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        return new User(
                preferences.getString(EMAIL, "Email"),
                preferences.getString(FULL_NAME, "Full Name"),
                preferences.getString(USER_NAME, "Username"),
                preferences.getString(USER_TYPE, "Type User"),
                preferences.getString(PATH_IMG_US, "Image Upload")
        );
    }
}
