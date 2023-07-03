package com.isc.hermes.model.User;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.isc.hermes.database.AccountInfoManager;
import org.json.JSONException;
import java.util.concurrent.ExecutionException;

/**
 * Runnable implementation for updating the user repository using a database.
 */
public class UserRepositoryUpdaterUsingDBRunnable implements Runnable{

    /**
     * Executes the update process for the user repository using a database.
     *
     * @throws ExecutionException if there is an error while executing the update process
     * @throws InterruptedException if the update process is interrupted
     * @throws JSONException if there is an error parsing JSON data
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
