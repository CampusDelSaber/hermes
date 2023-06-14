package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This class represents the AccountInformation activity, which displays information about the account.
 */
public class AccountInformation extends AppCompatActivity {

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState The saved instance state bundle, containing the activity's previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
    }

    /**
     * Navigates to the principal view within the current activity.
     *
     * @param view The view that triggers the navigation.
     */
    public void goToPrincipalView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}