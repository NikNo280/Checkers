package com.example.checkers.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.checkers.Adapter.StatisticAdapter;
import com.example.checkers.R;
import com.example.checkers.ViewModel.StatisticViewModel;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ListView statisticList = (ListView) findViewById(R.id.statisticList);
        StatisticViewModel statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel.class);
        statisticViewModel.readStatistic();
        statisticViewModel.getListStatistics().observe(this, list -> {
            StatisticAdapter stateAdapter = new StatisticAdapter(getApplicationContext(), R.layout.statistic, list);
            statisticList.setAdapter(stateAdapter);
        });
    }
}