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
    private final MutableLiveData<Boolean> isQueen = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isChecker = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isStep = new MutableLiveData<>();
    private final MutableLiveData<Integer> countChecker = new MutableLiveData<>();
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
        endStep();
        countChecker.setValue(0);
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

    public void setPoint(String index)
    {
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
                    isCheckerSelected.setValue(true);
                    isQueen.setValue(false);
                    return;
                }
                else if (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()) == IconEnum.WHITE_QUEEN.getMapCode())
                {
                    positionChecker.setValue(index);
                    isCheckerSelected.setValue(true);
                    isQueen.setValue(true);
                    return;
                }
                else if (isCheckerSelected.getValue())
                {
                    if (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue()).toString()) == IconEnum.BLACK_MAP.getMapCode())
                    {
                        positionToMove.setValue(index);
                        move();
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
        updateMap();
    }

    private void attackChecker(int position)
    {
        int[] tempMap = map.getValue();
        if (tempMap[position] == IconEnum.BLACK_CHECKER.getImageCode() ||
            tempMap[position] == IconEnum.BLACK_QUEEN.getImageCode())
        {
            updatePositionDB();
            Map<String, Object> values = new HashMap<>();
            values.put(String.valueOf(position), IconEnum.BLACK_MAP.getMapCode());
            firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
            countChecker.setValue(countChecker.getValue() + 1);
            changePosition();
            turnedQueen();
            int[] temp = map.getValue();
            temp[position] = IconEnum.BLACK_MAP.getImageCode();
            map.setValue(temp);
            updateMap();
            isChecker.setValue(false);
            isStep.setValue(true);
        }
    }

    private void turnedQueen()
    {
        if (    positionToMove.getValue().equals("1") ||
                positionToMove.getValue().equals("3") ||
                positionToMove.getValue().equals("5") ||
                positionToMove.getValue().equals("7")) {
            Map<String, Object> values = new HashMap<>();
            values.put(positionToMove.getValue(), IconEnum.WHITE_QUEEN.getMapCode());
            firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
            int[] temp = map.getValue();
            temp[Integer.parseInt(positionToMove.getValue())] = IconEnum.WHITE_QUEEN.getImageCode();
            map.setValue(temp);
            isStep.setValue(true);
        }
    }

    private void move()
    {
        if (!isQueen.getValue())
        {
            int positionMinus7 = Integer.parseInt(positionChecker.getValue()) - 7;
            int positionMinus9 = Integer.parseInt(positionChecker.getValue()) - 9;
            int positionPlus7 = Integer.parseInt(positionChecker.getValue()) + 7;
            int positionPlus9 = Integer.parseInt(positionChecker.getValue()) + 9;
            if (positionMinus7 == Integer.parseInt(positionToMove.getValue()) ||
                    positionMinus9  == Integer.parseInt(positionToMove.getValue()))
            {
                if (!isStep.getValue())
                {
                    updatePositionDB();
                    changePosition();
                    turnedQueen();
                    updateMap();
                    Map<String, Object> values = new HashMap<>();
                    values.put(pathStep, "visitor");
                    firebaseDatabase.updateChild(pathRooms + "/" + roomName, values);
                    endStep();
                }
            }
            else if (Integer.parseInt(positionChecker.getValue()) - 18  == Integer.parseInt(positionToMove.getValue()))
            {
                attackChecker(positionMinus9);
                endStep();
                isStep.setValue(true);
            }
            else if(Integer.parseInt(positionChecker.getValue()) - 14 == Integer.parseInt(positionToMove.getValue()))
            {
                attackChecker(positionMinus7);
                endStep();
                isStep.setValue(true);
            }
            else if (Integer.parseInt(positionChecker.getValue()) + 18  == Integer.parseInt(positionToMove.getValue()))
            {
                attackChecker(positionPlus9);
                endStep();
                isStep.setValue(true);
            }
            else if(Integer.parseInt(positionChecker.getValue()) + 14 == Integer.parseInt(positionToMove.getValue()))
            {
                attackChecker(positionPlus7);
                endStep();
                isStep.setValue(true);
            }
        }
        else
        {
            int indexChecker = Integer.parseInt(positionChecker.getValue());
            int indexMove = Integer.parseInt(positionToMove.getValue());
            boolean up7 = (indexChecker - indexMove) % 7 == 0;
            boolean up9 = (indexChecker - indexMove) % 9 == 0;
            int positionToDelete = 0;
            int countToDelete = 0;
            if (up7 || up9)
            {
                int syllable;
                if (up7)
                {
                    syllable = 7;
                }
                else
                {
                    syllable = 9;
                }
                if (indexChecker < indexMove)
                {
                    syllable *= (-1);
                }
                while (indexChecker != indexMove)
                {
                    indexMove += syllable;
                    int[] temp = map.getValue();
                    if (temp[indexMove] == IconEnum.BLACK_CHECKER.getImageCode() ||
                        temp[indexMove] == IconEnum.BLACK_QUEEN.getImageCode())
                    {
                        countToDelete += 1;
                        positionToDelete = indexMove;
                    }
                }
                if (countToDelete == 0 && !isStep.getValue())
                {
                    updatePositionDB();
                    changePosition();
                    updateMap();
                    Map<String, Object> values = new HashMap<>();
                    values.put(pathStep, "visitor");
                    firebaseDatabase.updateChild(pathRooms + "/" + roomName, values);
                    endStep();
                }
                else if (countToDelete == 1)
                {
                    updatePositionDB();
                    changePosition();
                    Map<String, Object> values = new HashMap<>();
                    values.put(String.valueOf(positionToDelete), IconEnum.BLACK_MAP.getMapCode());
                    firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
                    countChecker.setValue(countChecker.getValue() + 1);
                    int[] temp = map.getValue();
                    temp[positionToDelete] = IconEnum.BLACK_MAP.getImageCode();
                    map.setValue(temp);
                    updateMap();
                    endStep();
                    isStep.setValue(true);
                }
            }
        }
    }

    private void updatePositionDB()
    {
        Map<String, Object> values = new HashMap<>();
        values.put(positionChecker.getValue(), IconEnum.BLACK_MAP.getMapCode());
        firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
        values = new HashMap<>();
        int[] temp = map.getValue();
        if(temp[Integer.parseInt(positionChecker.getValue())] == IconEnum.WHITE_CHECKER.getImageCode())
        {
            values.put(positionToMove.getValue(), IconEnum.WHITE_CHECKER.getMapCode());
        }
        else
        {
            values.put(positionToMove.getValue(), IconEnum.WHITE_QUEEN.getMapCode());
        }
        firebaseDatabase.updateChild(pathRooms + "/" + roomName + "/" + pathMap, values);
    }

    private void changePosition()
    {
        int[] temp = map.getValue();
        if(temp[Integer.parseInt(positionChecker.getValue())] == IconEnum.WHITE_CHECKER.getImageCode())
        {
            temp[Integer.parseInt(positionToMove.getValue())] = IconEnum.WHITE_CHECKER.getImageCode();
        }
        else
        {
            temp[Integer.parseInt(positionToMove.getValue())] = IconEnum.WHITE_QUEEN.getImageCode();
        }
        temp[Integer.parseInt(positionChecker.getValue())] = IconEnum.BLACK_MAP.getImageCode();
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

    private void endStep()
    {
        isChecker.setValue(false);
        isStep.setValue(false);
        isQueen.setValue(false);
        isCheckerSelected.setValue(false);
        positionChecker.setValue("-1");
        positionToMove.setValue("-1");
    }

    public void endStepInBtn()
    {
        if (isStep.getValue())
        {
            isStep.setValue(false);
            Map<String, Object> values = new HashMap<>();
            values.put(pathStep, "visitor");
            firebaseDatabase.updateChild(pathRooms + "/" + roomName, values);
        }
    }
    public LiveData<Integer> getCountChecker() {
        return countChecker;
    }

    public void addStatistics() {
        Map<String, Object> values = new HashMap<>();
        firebaseDatabase.getReference(pathRooms + "/" + roomName + "/p1").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (firebaseAuth.getUserUID().equals(dataSnapshot.child("user").getValue(String.class)))
                        {
                            values.put("Результат", "Победа");
                        }
                        else
                        {
                            values.put("Результат", "Поражение");
                        }
                        firebaseDatabase.updateChild("Statistics/" + dataSnapshot.child("user").getValue(String.class) + "/" + roomName, values);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        firebaseDatabase.getReference(pathRooms + "/" + roomName + "/p2").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (firebaseAuth.getUserUID().equals(dataSnapshot.child("user").getValue(String.class)))
                        {
                            values.put("Результат", "Победа");
                        }
                        else
                        {
                            values.put("Результат", "Поражение");
                        }
                        firebaseDatabase.updateChild("Statistics/" + dataSnapshot.child("user").getValue(String.class) + "/" + roomName, values);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
