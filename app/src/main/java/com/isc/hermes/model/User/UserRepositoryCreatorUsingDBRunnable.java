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
public class UserRepositoryCreatorUsingDBRunnable implements Runnable {

    private final User user;

    /**
     * Creates a new UserRepositoryCreatorUsingDBRunnable object with the specified user.
     *
     * @param user the User object to be used for creating the UserRepository
     */
    public UserRepositoryCreatorUsingDBRunnable(User user) {
        this.user = user;
    }

    /**
     * Executes the logic to create a new user in the {@link UserRepository} using a database.
     * <p>
     * The method adds the user's information, such as email, full name, username, user type,
     * and image path, to the database using an {@link AccountInfoManager}. It then retrieves the
     * generated ID for the user's email using the {@link AccountInfoManager#getIdByEmail} method
     * and sets it in the user object using the {@link User#setId} method. Finally, it updates the
     * {@link UserRepository} with the created user using the {@link UserRepository#setUserContained} method.
     *
     * @throws RuntimeException if an {@link ExecutionException}, {@link InterruptedException},
     *                          or {@link JSONException} occurs during the process.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        AccountInfoManager manager = new AccountInfoManager();
        try {
            manager.addUser(user.getEmail(), user.getFullName(), user.getUserName(), user.getTypeUser(), user.getPathImageUser());
            user.setId(manager.getIdByEmail(user.getEmail()));
            UserRepository.getInstance().setUserContained(user);
        } catch (ExecutionException | InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
