package com.example.checkers.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.checkers.Model.StatisticsModel;
import com.example.checkers.R;

import java.util.List;

public class StatisticAdapter extends ArrayAdapter<StatisticsModel> {

    private final LayoutInflater inflater;
    private final int layout;
    private final List<StatisticsModel> statistics;

    public StatisticAdapter(Context context, int resource, List<StatisticsModel> statistics) {
        super(context, resource, statistics);
        this.statistics = statistics;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = inflater.inflate(this.layout, parent, false);
        TextView roomNameET = (TextView) view.findViewById(R.id.roomNameET);
        TextView resultET = (TextView) view.findViewById(R.id.resultET);
        StatisticsModel statistic = statistics.get(position);
        roomNameET.setText(statistic.getNameRoom());
        resultET.setText(statistic.getResult());
        return view;
    }

}
