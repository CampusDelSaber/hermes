package com.isc.hermes.model.User;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.isc.hermes.database.AccountInfoManager;
import org.json.JSONException;
import java.util.concurrent.ExecutionException;

public class UserRepositoryUpdaterUsingDBRunnable implements Runnable{

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
