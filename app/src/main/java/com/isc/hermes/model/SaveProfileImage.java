package com.isc.hermes.model;

import android.content.Context;
import android.net.Uri;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.isc.hermes.R;
import com.isc.hermes.model.User.UserRepository;

import de.hdodenhof.circleimageview.CircleImageView;

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
     */
    public void saveProfileImage(Uri uri, Button button, CircleImageView circleImageView, Context context) {
        button.setEnabled(false);
        Glide.with(context).load(R.drawable.loading).into(circleImageView);
        StorageReference filePath = storageReference.child(FOLDER_NAME).child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            filePath.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String imageURL = downloadUri.toString();
                UserRepository.getInstance().getUserContained().setPathImageUser(imageURL);
                button.setEnabled(true);
                Glide.with(context).load(imageURL).into(circleImageView);
            });
        });
    }

}
