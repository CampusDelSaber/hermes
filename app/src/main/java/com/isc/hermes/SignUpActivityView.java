package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/**
 * Class for displaying a log in view.
 */
public class SignUpActivityView extends AppCompatActivity {

    /**
     * Method for creating the view and configuring it using the components xml.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_view);
    }
}