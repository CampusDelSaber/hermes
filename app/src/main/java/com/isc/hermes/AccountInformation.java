package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/**
 * This class represents the AccountInformation activity, which displays information about the account.
 */
public class AccountInformation extends AppCompatActivity {

    private Button buttonUploadImage;
    private Button buttonSaveInformation;
    private Button buttonEditInformation;
    private Button buttonDeleteAccount;
    private AutoCompleteTextView textFieldUserName;
    private AutoCompleteTextView textFieldFullName;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState The saved instance state bundle, containing the activity's previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        assignValuesToComponentsView();
    }

    private void  assignValuesToComponentsView() {
        buttonSaveInformation =  findViewById(R.id.buttonSaveInformation);
        buttonUploadImage =  findViewById(R.id.buttonUploadImage);
        buttonEditInformation =  findViewById(R.id.buttonEditInformation);
        buttonDeleteAccount =  findViewById(R.id.buttonDeleteAccount);
        textFieldFullName = findViewById(R.id.textFieldFullName);
        textFieldUserName = findViewById(R.id.textFieldUserName);
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
    public void deleteAccountAction(View view) {
        System.out.println("the action for delete account will be here");
    }

    /**
     * This method is used to upload the image account information using on click action.
     */
    public void uploadImageProfileAction(View view) {
        System.out.println("the action for upload profile picture will be here");
    }

    /**
     * This method is used to edit an account information using on click action.
     */
    public void editAccountAction(View view) {
        buttonSaveInformation.setVisibility(View.VISIBLE);
        textFieldUserName.setInputType(InputType.TYPE_CLASS_TEXT);
        textFieldFullName.setInputType(InputType.TYPE_CLASS_TEXT);
        System.out.println(textFieldFullName.getText());
    }

    /**
     * This method is used to edit an account information using on click action.
     */
    public void saveAccountInformationAction(View view) {
        buttonSaveInformation.setVisibility(View.INVISIBLE);
        textFieldUserName.setInputType(InputType.TYPE_NULL);
        textFieldFullName.setInputType(InputType.TYPE_NULL);
    }
}