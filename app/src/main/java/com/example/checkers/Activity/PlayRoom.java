package com.example.checkers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.checkers.Interfase.IMap;
import com.example.checkers.R;
import com.example.checkers.ViewModel.HostViewModel;
import com.example.checkers.ViewModel.VisitorViewModel;

import java.util.Objects;

public class PlayRoom extends AppCompatActivity {

    IMap playRoomViewModel;
    EditText stepET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_room);

        stepET = findViewById(R.id.stepET);
        Intent intent = Objects.requireNonNull(this).getIntent();
        if (intent.getStringExtra("RoomRole").equals("host")) {
            playRoomViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(HostViewModel.class);
        } else {
            playRoomViewModel = ViewModelProviders.of(Objects.requireNonNull(this)).get(VisitorViewModel.class);
        }
        playRoomViewModel.initialization(intent.getStringExtra("RoomName"));
        playRoomViewModel.getStepET().observe(this, v -> {
            stepET.setText(v);
        });
        playRoomViewModel.getCountChecker().observe(this, v -> {
            if (v  == 12)
            {
                playRoomViewModel.addStatistics();
                finish();
            }
        });
    }

    public void btnNext(View view) {
        playRoomViewModel.endStepInBtn();
    }
}