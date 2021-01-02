package com.example.checkers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.checkers.R;
import com.example.checkers.ViewModel.MainMenuViewModel;

public class MainMenu extends AppCompatActivity {

    private MainMenuViewModel mainMenuViewModel;
    private EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        textView = findViewById(R.id.roomNameET);
        mainMenuViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        mainMenuViewModel.getIsConnect().observe(this, v -> {
            Intent intent = new Intent(MainMenu.this, PlayRoom.class);
            intent.putExtra("RoomName", mainMenuViewModel.getRoomNameLiveData().getValue());
            intent.putExtra("RoomRole", mainMenuViewModel.getRoomRole().getValue());
            startActivity(intent);
        });
    }

    public void bntProfile(View view) {
        Intent intent = new Intent(MainMenu.this, Profile.class);
        startActivity(intent);
    }

    public void bntCreate(View view) {
        mainMenuViewModel.createRoom(textView.getText().toString());
    }

    public void bntConnect(View view) {
        mainMenuViewModel.connectToRoom(textView.getText().toString());
    }

    public void bntStatistics(View view) {
        Intent intent = new Intent(MainMenu.this, Statistics.class);
        startActivity(intent);
    }
}