package com.example.checkers.Interfase;

import androidx.lifecycle.LiveData;

public interface IMap {
    LiveData<int[]> getMap();
    void setPoint(String index);
    void initialization(String roomName);
    LiveData<String> getPositionChecker();
    LiveData<String> getPositionToMove();
    LiveData<String> getStepET();
}
