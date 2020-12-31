package com.example.checkers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.checkers.R;
import com.example.checkers.ViewModel.PlayRoomViewModel;

public class PlayRoom extends AppCompatActivity {

    PlayRoomViewModel playRoomViewModel;
    EditText stepET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_room);

        Intent intent = getIntent();
        stepET = findViewById(R.id.stepET);
        playRoomViewModel = ViewModelProviders.of(this).get(PlayRoomViewModel.class);
        playRoomViewModel.setRoomNameLiveData(intent.getStringExtra("RoomName"));
        playRoomViewModel.getStep().observe(this, v -> {
            stepET.setText(v);
        });
    }
}