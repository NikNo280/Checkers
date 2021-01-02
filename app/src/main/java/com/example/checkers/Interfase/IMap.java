package com.example.checkers.Interfase;

import androidx.lifecycle.LiveData;

public interface IMap {
    LiveData<int[]> getMap();
    void setPoint(String index);
    void initialization(String roomName);
    LiveData<String> getStepET();
    void endStepInBtn();
    LiveData<Integer> getCountChecker();
    void addStatistics();

}
