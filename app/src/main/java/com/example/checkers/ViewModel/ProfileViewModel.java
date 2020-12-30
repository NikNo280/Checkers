package com.example.checkers.ViewModel;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.checkers.Model.FirebaseAuthenticationModel;
import com.example.checkers.Model.FirebaseDatabaseModel;
import com.example.checkers.Model.FirebaseStorageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isGravatar = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> imagePath = new MutableLiveData<>();
    private final MutableLiveData<Uri> imgUri = new MutableLiveData<>();

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseStorageModel firebaseStorage;
    FirebaseDatabaseModel firebaseDatabase;

    String pathToRoom;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseStorage = new FirebaseStorageModel();
        firebaseDatabase = new FirebaseDatabaseModel();
        pathToRoom = "Users/" + firebaseAuth.getUserUID();
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getImagePath() {
        return imagePath;
    }

    public LiveData<Boolean> isGravatar() {
        return isGravatar;
    }

    public LiveData<Uri> getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri Image) {
        imgUri.setValue(Image);
    }

    public void initializationInformation() {
        firebaseDatabase.getReference(pathToRoom).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (Objects.equals(child.getKey(), "Gravatar")) {
                        isGravatar.setValue(Boolean.parseBoolean(Objects.requireNonNull(child.getValue()).toString()));
                    } else if (Objects.equals(child.getKey(), "userName")) {
                        userName.setValue(Objects.requireNonNull(child.getValue()).toString());
                    }
                    else {
                        imagePath.setValue(Objects.requireNonNull(child.getValue()).toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void uploadImage() {
        Uri imageUri = imgUri.getValue();
        if (imageUri != null) {
            StorageReference ref = firebaseStorage.getReference(System.currentTimeMillis() + "." + getImage(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getApplication(), "Фотография сохранена", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Image", uri.toString());
                    firebaseDatabase.updateChild(pathToRoom, childUpdates);
                });
            });
        } else {
            Toast.makeText(getApplication(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }


    public String getImage(Uri uri) {
        ContentResolver contentResolver = getApplication().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void setName(String newName) {
        if (!newName.isEmpty()) {
            Query query = firebaseDatabase.getReference("Users").orderByChild("userName").equalTo(newName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        firebaseDatabase.setValue(pathToRoom + "/userName", newName);
                        Toast.makeText(getApplication(), "Имя сохранено", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getApplication(), "Заполните имя", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeButton(boolean gravatar) {
        Map<String, Object> childUpdates = new HashMap<>();
        if (gravatar) {
            String hash = md5(firebaseAuth.getEmail());
            String gravatarUrl = "https://s.gravatar.com/avatar/" + hash + "?s=80";
            imagePath.setValue(gravatarUrl);
            childUpdates.put("Image", gravatarUrl);
            childUpdates.put("Gravatar", true);
        } else {
            String urlImage = "https://firebasestorage.googleapis.com/v0/b/checkers-2162e.appspot.com/o/0b37d82bcfd11cb3196fa5329f3bff0f.jpg?alt=media&token=37ef771c-0724-4078-ae88-c87d3428c49f";
            imagePath.setValue(urlImage);
            childUpdates.put("Image", urlImage);
            childUpdates.put("Gravatar", false);
        }
        firebaseDatabase.updateChild(pathToRoom, childUpdates);
    }

    private String md5(String in) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            BigInteger bigInt = new BigInteger(1, digest.digest());
            result = bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
