package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.isc.hermes.controller.DeleteAccountWarningDialog;

/**
 * This class represents the AccountInformation activity, which displays information about the account.
 */
public class AccountInformation extends AppCompatActivity {

    private DeleteAccountWarningDialog dialog;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState The saved instance state bundle, containing the activity's previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        deleteAccountAction();
        uploadImageProfileAction();
        editAccountAction();
        initializeWarningPopup();
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

    /**
     * This method is used to delete an account using on click action.
     */
    public void deleteAccountAction() {
        findViewById(R.id.buttonDeleteAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    /**
     * This method is used to upload the image account information using on click action.
     */
    public void uploadImageProfileAction() {
        findViewById(R.id.buttonUploadImage).setOnClickListener( v ->
                System.out.println("the action for upload profile picture will be here"));
    }

    /**
     * This method is used to edit an account information using on click action.
     */
    public void editAccountAction() {
        findViewById(R.id.buttonEditInformation).setOnClickListener( v ->
                System.out.println("the action for edit account information will be here"));
    }

    /**
     * This method initialize the popup warning when we pressed on the delete account button
     */
    private void initializeWarningPopup(){
        this.dialog = new DeleteAccountWarningDialog(this);
    }
}