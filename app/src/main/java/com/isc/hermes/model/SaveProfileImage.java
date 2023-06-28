package com.isc.hermes.model;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.model.User.UserRepository;

/**
 * Utility class for saving a profile image to Firebase Storage and updating the user's path image.
 */
public class SaveProfileImage {

    private static final String FOLDER_NAME = "Profile_images";
    private static final String MESSAGE = "Image uploaded successfully!";
    private StorageReference storageReference;

    /**
     * Constructs a new SaveProfileImage instance.
     */
    public SaveProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Saves the profile image to Firebase Storage and updates the user's path image.
     *
     * @param uri     The Uri of the image to be saved.
     * @param context The context in which the method is called.
     */
    public void saveProfileImage(Uri uri, Context context) {
        StorageReference filePath = storageReference.child(FOLDER_NAME).child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            filePath.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String imageURL = downloadUri.toString();
                setPathImage(imageURL);
            });
            Toast.makeText(context, MESSAGE, Toast.LENGTH_SHORT).show();
        });
    }



    /**
     * Sets the path of the profile image for the current user and updates the user information.
     *
     * @param path The path of the profile image.
     */
    private void setPathImage(String path) {
        UserRepository.getInstance().getUserContained().setPathImageUser(path);
        AccountInfoManager accountInfoManager = new AccountInfoManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            accountInfoManager.editUser(UserRepository.getInstance().getUserContained());
        }
    }

}
