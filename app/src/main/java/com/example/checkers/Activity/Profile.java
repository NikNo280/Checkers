package com.example.checkers.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.checkers.R;
import com.example.checkers.ViewModel.ProfileViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private ProfileViewModel userPageViewModel;

    ImageView imageView;
    Button updateName;
    EditText editText;

    public static final int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userPageViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        editText = findViewById(R.id.NameUser);
        updateName = findViewById(R.id.ApplyName);
        imageView = findViewById(R.id.imageView);

        userPageViewModel.initializationInformation();

        userPageViewModel.getImagePath().observe(this, image -> {
            Picasso.with(getApplicationContext())
                    .load(image)
                    .into(imageView);
        });
        userPageViewModel.getImgUri().observe(this, uri -> {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        userPageViewModel.isGravatar().observe(this, status -> {
            if (status) {
                ((RadioButton) findViewById(R.id.btnGravatar)).setChecked(true);
                findViewById(R.id.ChooseImage).setVisibility(View.INVISIBLE);
                findViewById(R.id.UpdateImage).setVisibility(View.INVISIBLE);
            } else {
                ((RadioButton) findViewById(R.id.btnFireBase)).setChecked(true);
                findViewById(R.id.ChooseImage).setVisibility(View.VISIBLE);
                findViewById(R.id.UpdateImage).setVisibility(View.VISIBLE);
            }
        });
        userPageViewModel.getUserName().observe(this, userName -> editText.setHint(userName));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            userPageViewModel.setImgUri(data.getData());
        }
    }

    public void btnOpenGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображние"), REQUEST_CODE);
    }

    public void btnUploadImage(View view) {
        userPageViewModel.uploadImage();
    }

    public void radioBtn(View view) {
        switch (view.getId()) {
            case R.id.btnFireBase:
                findViewById(R.id.ChooseImage).setVisibility(View.VISIBLE);
                findViewById(R.id.UpdateImage).setVisibility(View.VISIBLE);
                userPageViewModel.changeButton(false);
                break;
            case R.id.btnGravatar:
                findViewById(R.id.ChooseImage).setVisibility(View.INVISIBLE);
                findViewById(R.id.UpdateImage).setVisibility(View.INVISIBLE);
                userPageViewModel.changeButton(true);
                break;
        }
    }

    public void btnUpdateName(View view) {
        updateName.setOnClickListener(v -> userPageViewModel.setName(editText.getText().toString()));
    }
}