package com.isc.hermes.model.User;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.isc.hermes.database.AccountInfoManager;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/**
 * A {@code Runnable} implementation that creates a new user in the {@link UserRepository}
 * by adding the user to the database using an {@link AccountInfoManager}.
 * <p>
 * The {@code run} method adds the user's information, such as email, full name, username, user type,
 * and image path, to the database using an {@link AccountInfoManager}. It then retrieves the generated
 * ID for the user's email and sets it in the user object. Finally, it updates the {@code UserRepository}
 * with the created user using the {@link UserRepository#setUserContained} method.
 */
public class UserRepositoryEditorUsingDBRunnable implements Runnable {

    private final User user;


    public UserRepositoryEditorUsingDBRunnable(User user) {
        this.user = user;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        AccountInfoManager manager = new AccountInfoManager();
        manager.editUser(user);
    }
}
