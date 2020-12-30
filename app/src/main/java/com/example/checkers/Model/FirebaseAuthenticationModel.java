package com.example.checkers.Model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthenticationModel {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthenticationModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }

    public String getUserUID() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }
}
