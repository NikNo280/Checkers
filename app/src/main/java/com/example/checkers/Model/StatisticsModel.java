package com.example.checkers.Model;

public class StatisticsModel {

    private final String nameRoom;
    private final String result;

    public StatisticsModel(String nameRoom, String result) {
        this.nameRoom = nameRoom;
        this.result = result;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public String getResult() {
        return result;
    }
}