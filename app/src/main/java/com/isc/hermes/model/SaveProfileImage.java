package com.isc.hermes.model;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.isc.hermes.AccountInformation;

public class SaveProfileImage {

    private static final String FOLDER_NAME = "Profile_images";
    private static final String MESSAGE = "Image uploaded successfully!";
    private StorageReference storageReference;

    public SaveProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void saveProfileImage(Uri uri, Context context) {
        StorageReference filePath = storageReference.child(FOLDER_NAME).child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        System.out.println(imageURL);
                    }
                });
                Toast.makeText(context, MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
