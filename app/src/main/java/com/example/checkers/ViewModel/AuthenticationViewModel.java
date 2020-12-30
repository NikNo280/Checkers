package com.example.checkers.ViewModel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.checkers.Model.FirebaseAuthenticationModel;
import com.example.checkers.Model.FirebaseDatabaseModel;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationViewModel extends AndroidViewModel{
    private final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;

    public LiveData<Boolean> isSuccessful() {
        return isSuccessful;
    }

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseDatabase = new FirebaseDatabaseModel();
    }

    public void Registration(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplication(), "Регистрация провалена", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isEmailValid(email)) {
            Toast.makeText(getApplication(), "Регистрация провалена", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> values = new HashMap<>();
                values.put("Gravatar", false);
                values.put("userName", firebaseAuth.getUserUID());
                values.put("Image", "https://firebasestorage.googleapis.com/v0/b/checkers-2162e.appspot.com/o/0b37d82bcfd11cb3196fa5329f3bff0f.jpg?alt=media&token=37ef771c-0724-4078-ae88-c87d3428c49f");
                firebaseDatabase.updateChild("Users/" + firebaseAuth.getUserUID(), values);
                isSuccessful.setValue(true);
                Toast.makeText(getApplication(), "Регистрация успешна", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "Регистрация провалена", Toast.LENGTH_SHORT).show();
                isSuccessful.setValue(false);
            }
        });
    }

    public void Login(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplication(), "Авторизация провалена", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> isSuccessful.setValue(task.isSuccessful()));
        return;
    }

    private Boolean isEmailValid(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}

