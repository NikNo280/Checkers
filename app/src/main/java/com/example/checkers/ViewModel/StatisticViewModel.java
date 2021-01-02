package com.example.checkers.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.checkers.Model.FirebaseAuthenticationModel;
import com.example.checkers.Model.FirebaseDatabaseModel;
import com.example.checkers.Model.StatisticsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticViewModel extends AndroidViewModel {

    private final MutableLiveData<List<StatisticsModel>> listStatistics = new MutableLiveData<>();

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;


    public StatisticViewModel(@NonNull Application application) {
        super(application);
        firebaseDatabase = new FirebaseDatabaseModel();
        firebaseAuth = new FirebaseAuthenticationModel();
    }

    public LiveData<List<StatisticsModel>> getListStatistics() {
        return listStatistics;
    }

    public void readStatistic() {
        List<StatisticsModel> temp = new ArrayList<>();
        firebaseDatabase.getReference("Statistics/" + firebaseAuth.getUserUID()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot roomNameTable : dataSnapshot.getChildren())
                        {
                            for (DataSnapshot results: roomNameTable.getChildren())
                            {
                                temp.add(new StatisticsModel("Название комнаты : " + roomNameTable.getKey(),
                                        results.getKey() + " : "+ results.getValue().toString()));
                            }
                        }
                        listStatistics.setValue(temp);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
