package com.example.checkers.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.checkers.Enum.IconEnum;
import com.example.checkers.Model.FirebaseAuthenticationModel;
import com.example.checkers.Model.FirebaseDatabaseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainMenuViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    private final MutableLiveData<String> roomNameLiveData = new MutableLiveData<>();
    private final String pathRooms = "Rooms";
    private final String pathUser = "user";
    private final String pathChecker = "checker";
    private final String pathRole = "role";
    private final String pathMap = "map";

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;

    public MainMenuViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseDatabase = new FirebaseDatabaseModel();
    }

    public LiveData<Boolean> getIsConnect() {
        return isConnect;
    }
    public LiveData<String> getRoomNameLiveData() {
        return roomNameLiveData;
    }

    public void createRoom(String roomName) {
        if (roomName.isEmpty()) {
            Toast.makeText(getApplication(), "Введите название комнаты", Toast.LENGTH_SHORT).show();
            return;
        }
        roomNameLiveData.setValue(roomName);
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i < 3)
                {
                    if(i % 2 == j % 2)
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.WHITE_MAP.getMapCode());
                    }
                    else
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.BLACK_CHECKER.getMapCode());
                    }
                }
                else if (i < 5)
                {
                    if(i % 2 == j % 2)
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.WHITE_MAP.getMapCode());;
                    }
                    else
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.BLACK_MAP.getMapCode());
                    }
                }
                else
                {
                    if(i % 2 == j % 2)
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.WHITE_MAP.getMapCode());
                    }
                    else
                    {
                        values.put(String.valueOf(i * 8 + j), IconEnum.WHITE_CHECKER.getMapCode());
                    }
                }
            }
        }
        firebaseDatabase.updateChild("/"+ pathRooms + "/" + roomName + "/" + pathMap, values);
        values = new HashMap<>();
        values.put(pathUser, firebaseAuth.getUserUID());
        values.put(pathChecker, 12);
        values.put(pathRole, "host");
        firebaseDatabase.updateChild("/"+ pathRooms + "/" + roomName + "/p1", values);
        isConnect.setValue(true);
    }

    public void connectToRoom(String roomName) {
        if (roomName.isEmpty()) {
            Toast.makeText(getApplication(), "Введите название комнаты", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseDatabase.getReference(pathRooms + "/" + roomName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> values = new HashMap<>();
                    values.put(pathUser, firebaseAuth.getUserUID());
                    values.put(pathChecker, 12);
                    values.put(pathRole, "visitor");
                    firebaseDatabase.updateChild("/"+ pathRooms + "/" + roomName + "/p2", values);
                    isConnect.setValue(true);
                } else {
                    Toast.makeText(getApplication(), "Комнаты не существует", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //TODO
    public void removeRoom() {
        firebaseDatabase.remove(pathRooms + "/" + roomNameLiveData.getValue());
    }
}
