package com.example.checkers.Model;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageModel {

    private final FirebaseStorage firebaseStorage;

    public FirebaseStorageModel() {
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public StorageReference getReference(String path) {
        return firebaseStorage.getReference(path);
    }
}
