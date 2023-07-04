package com.isc.hermes.model.User;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.isc.hermes.database.AccountInfoManager;
import org.json.JSONException;
import java.util.concurrent.ExecutionException;

/**
 * A {@code Runnable} implementation that updates the user information in the {@link UserRepository}
 * by retrieving it from the database using an {@link AccountInfoManager}.
 * <p>
 * The {@code run} method fetches the user ID associated with the current user's email from the database,
 * retrieves the user information using the ID, and updates the user information in the {@code UserRepository}.
 * The updated user information is then printed to the console.
 */
public class UserRepositoryUpdaterUsingDBRunnable implements Runnable{

    /**
     * Executes the logic to update user information in the {@link UserRepository} using a database.
     * <p>
     * The method fetches the user ID associated with the current user's email from the database
     * using an {@link AccountInfoManager}. It then retrieves the user information using the ID
     * and updates the user information in the {@link UserRepository} using the {@code setUserContained} method.
     * The updated user information is printed to the console using the {@link System#out#println} method.
     *
     * @throws RuntimeException if an {@link ExecutionException}, {@link InterruptedException},
     *                          or {@link JSONException} occurs during the process.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        AccountInfoManager manager = new AccountInfoManager();
        try {
            String id = manager.getIdByEmail(UserRepository.getInstance().getUserContained().getEmail());
            User user = new AccountInfoManager().getUserById(id);
            UserRepository.getInstance().setUserContained(user);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            throw new RuntimeException(e); }
    }
}
