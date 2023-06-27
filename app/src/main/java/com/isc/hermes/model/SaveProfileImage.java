package com.isc.hermes.model;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SaveProfileImage {

    private static final String FIREBASE_STORAGE_URL = "gs://hermes3a.appspot.com/Profile_images";
    private StorageReference storageReference;

    public SaveProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }



}
