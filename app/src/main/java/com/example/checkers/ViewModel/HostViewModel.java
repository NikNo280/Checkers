package com.example.checkers.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.checkers.Enum.IconEnum;
import com.example.checkers.Interfase.IMap;
import com.example.checkers.Model.FirebaseAuthenticationModel;
import com.example.checkers.Model.FirebaseDatabaseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HostViewModel extends AndroidViewModel implements IMap {
    private final MutableLiveData<int[]> map = new MutableLiveData<>();
    private final MutableLiveData<String> stepET = new MutableLiveData<>();
    private final MutableLiveData<String> positionChecker = new MutableLiveData<>();
    private final MutableLiveData<String> positionToMove = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isCheckerSelected = new MutableLiveData<>();
    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;
    private final String pathRooms = "Rooms";
    private final String pathMap = "map";
    private final String pathUser = "user";
    private final String pathChecker = "checker";
    private final String pathStep = "step";
    private String roomName = "";
    public HostViewModel(@NonNull Application application) {
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
                        tempMap[i * 8 + j] =  IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] =  IconEnum.BLACK_CHECKER.getImageCode();
                    }
                }
                else if (i < 5)
                {
                    if(i % 2 == j % 2)
                    {
                        tempMap[i * 8 + j] =  IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] =  IconEnum.BLACK_MAP.getImageCode();
                    }
                }
                else
                {
                    if(i % 2 == j % 2)
                    {
                        tempMap[i * 8 + j] =  IconEnum.WHITE_MAP.getImageCode();
                    }
                    else
                    {
                        tempMap[i * 8 + j] =  IconEnum.WHITE_CHECKER.getImageCode();
                    }
                }
            }
        }
        map.setValue(tempMap);
        stepET.setValue("Ваш ход");
    }

    public void initialization(String roomName)
    {
        this.roomName = roomName;
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
                        values.put(String.valueOf(i * 8 + j), IconEnum.WHITE_MAP.getMapCode());
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
        values.put(pathStep, "host");
        firebaseDatabase.updateChild("/"+ pathRooms + "/" + roomName + "/", values);
        isCheckerSelected.setValue(false);
    }

    public LiveData<int[]> getMap() {
        return map;
    }

    public LiveData<String> getPositionChecker() {
        return positionChecker;
    }

    public LiveData<String> getPositionToMove() {
        return positionToMove;
    }

    public void setPoint(String index)
    {
        updateMap();
        firebaseDatabase.getReference(pathRooms + "/" + roomName + "/" + pathStep).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(String.class).equals("host"))
                        {
                            stepET.setValue("Ваш ход");
                        }
                        else
                        {
                            stepET.setValue("Xод противника");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if (stepET.getValue().equals("Ваш ход"))
        firebaseDatabase.getReference(pathRooms + "/" + roomName + "/" + pathMap+ "/" + Integer.parseInt(index)).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()) == IconEnum.BLACK_CHECKER.getMapCode())
                {
                    return;
                }
                else if (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()) == IconEnum.WHITE_CHECKER.getMapCode())
                {
                    positionChecker.setValue(index);
                    positionToMove.setValue("-1");
                    isCheckerSelected.setValue(true);
                }
                else if (isCheckerSelected.getValue())
                {
                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()) == IconEnum.BLACK_MAP.getMapCode())
                    {
                        positionToMove.setValue(index);
                        updatePositionDB();
                        changePosition();
                        updateMap();
                        Map<String, Object> values = new HashMap<>();
                        values.put(pathStep, "visitor");
                        firebaseDatabase.updateChild(pathRooms + "/" + roomName, values);
                    }
                    isCheckerSelected.setValue(false);
                }
                else
                {
                    isCheckerSelected.setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updatePositionDB()
    {
        Map<String, Object> values = new HashMap<>();
        values.put(positionChecker.getValue(), IconEnum.BLACK_MAP.getMapCode());
        firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
        values = new HashMap<>();
        values.put(positionToMove.getValue(), IconEnum.WHITE_CHECKER.getMapCode());
        firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
    }

    private void changePosition()
    {
        int[] temp = map.getValue();
        temp[Integer.parseInt(positionChecker.getValue())] = IconEnum.BLACK_MAP.getImageCode();
        temp[Integer.parseInt(positionToMove.getValue())] = IconEnum.WHITE_CHECKER.getImageCode();
        map.setValue(temp);
    }

    public void updateMap()
    {
        int[] temp = map.getValue();
        firebaseDatabase.getReference(pathRooms + "/" + roomName + "/" + pathMap).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < 64; i++)
                        {
                            temp[i] = IconEnum.getImageCode(dataSnapshot.child(String.valueOf(i)).getValue(Integer.class));
                            map.setValue(temp);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public LiveData<String> getStepET() {
        return stepET;
    }
}