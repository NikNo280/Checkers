package com.example.checkers.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.checkers.R;
import com.example.checkers.ViewModel.AuthenticationViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEt, passwordEt;
    private AuthenticationViewModel authenticationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authenticationViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);
        authenticationViewModel.isSuccessful().observe(this, status -> {
            if (status) {
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_sign_in)
        {
            authenticationViewModel.Login(emailEt.getText().toString(), passwordEt.getText().toString());
        }
        else if (view.getId() == R.id.btn_registration)
        {
            authenticationViewModel.Registration(emailEt.getText().toString(), passwordEt.getText().toString());
        }
    }
}