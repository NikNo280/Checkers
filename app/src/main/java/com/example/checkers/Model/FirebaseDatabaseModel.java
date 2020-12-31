package com.example.checkers.Model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class FirebaseDatabaseModel {

    private final FirebaseDatabase database;

    public FirebaseDatabaseModel() {
        database = FirebaseDatabase.getInstance();
    }

    public void updateChild(String path, Map<String, Object> values) {
        database.getReference(path).updateChildren(values);
    }

    public void remove(String path) {
        database.getReference(path).removeValue();
    }

    public void setValue(String path, Object value) {
        database.getReference(path).setValue(value);
    }

    public DatabaseReference getReference (String path) {
        return database.getReference(path);
    }
}
