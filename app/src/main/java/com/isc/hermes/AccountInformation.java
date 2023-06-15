package com.isc.hermes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import com.isc.hermes.controller.Utils;
import com.isc.hermes.model.Utils.ImageBBUploader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


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
    private ImageView imageView;

    private static final int PICK_IMAGE_REQUEST = 1;


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
        imageView = findViewById(R.id.imageUpload);
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
     *
     * @param view The view that triggers the navigation.
     */
    public void uploadImageProfileAction(View view) {
        openGallery();
    }

    /**
     * Opens the gallery to select an image.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Handles the result of the activity for selecting an image from the gallery.
     *
     * @param requestCode The request code for the activity.
     * @param resultCode  The result code from the activity.
     * @param data        The intent containing the result data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                Bitmap croppedBitmap = Utils.cropToSquare(bitmap);

                imageView.setImageBitmap(croppedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to edit an account information using on click action.
     *
     * @param view The view that triggers the navigation.
     */
    public void editAccountAction(View view) {
        buttonSaveInformation.setVisibility(View.VISIBLE);
        textFieldFullName.setEnabled(false);
    }

    /**
     * This method is used to edit an account information using on click action.
     *
     * @param view The view that triggers the navigation.
     */
    public void saveAccountInformationAction(View view) {
        buttonSaveInformation.setVisibility(View.INVISIBLE);
        textFieldFullName.setEnabled(true);
        System.out.println(textFieldFullName.getText());
    }
}