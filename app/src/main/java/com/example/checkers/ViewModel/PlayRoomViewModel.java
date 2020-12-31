package com.example.checkers.ViewModel;

import android.app.Application;

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

public class PlayRoomViewModel extends AndroidViewModel {
    private final MutableLiveData<int[]> map = new MutableLiveData<>();
    private final MutableLiveData<String> step = new MutableLiveData<>();
    private final MutableLiveData<String> roomNameLiveData = new MutableLiveData<>();
    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;
    private final String pathRooms = "Rooms";
    private final String pathUser = "user";
    private final String pathChecker = "checker";
    private final String pathRole = "role";
    private final String pathMap = "map";
    public PlayRoomViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseDatabase = new FirebaseDatabaseModel();
        int[] tempMap = new int[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i < 3)
                {
                    if(i % 2 == j % 2)
                    {
                        tempMap[i * 8 + j] = IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] = IconEnum.BLACK_CHECKER.getImageCode();
                    }
                }
                else if (i < 5)
                {
                    if(i % 2 == j % 2)
                    {
                        tempMap[i * 8 + j] = IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] = IconEnum.BLACK_MAP.getImageCode();
                    }
                }
                else
                {
                    if(i % 2 == j % 2)
                    {
                        tempMap[i * 8 + j] = IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] = IconEnum.WHITE_CHECKER.getImageCode();
                    }
                }
            }
        }
        map.setValue(tempMap);
    }

    public LiveData<int[]> getMap() {
        return map;
    }
    public LiveData<String> getStep() {
        return step;
    }
    public void setRoomNameLiveData(String name)
    {
        roomNameLiveData.setValue(name);
    }
    public void setPoint(String index)
    {
        firebaseDatabase.getReference(pathRooms + "/" + roomNameLiveData.getValue() + "/" + pathMap+ "/" + Integer.parseInt(index)).
                addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            step.setValue(dataSnapshot.getValue().toString());
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }
}
