package com.example.checkers.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkers.Adapter.MapAdapter;
import com.example.checkers.Interfase.IMap;
import com.example.checkers.R;
import com.example.checkers.ViewModel.HostViewModel;
import com.example.checkers.ViewModel.VisitorViewModel;

import java.util.Objects;

public class MapFragment extends Fragment implements MapAdapter.ItemListener{

    protected String[] nameList = new String[64];
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    IMap playRoomViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent.getStringExtra("RoomRole").equals("host")) {
            playRoomViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HostViewModel.class);
        } else {
            playRoomViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(VisitorViewModel.class);
        }
        playRoomViewModel.initialization(intent.getStringExtra("RoomName"));
        playRoomViewModel.getMap().observe(Objects.requireNonNull(requireActivity()), v -> {
            MapAdapter mapAdapter = new MapAdapter(v, nameList, this);
            recyclerView.setAdapter(mapAdapter);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getContext(), 8);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 64; i++)
        {
            nameList[i] = String.valueOf(i);
        }
        return view;
    }

    @Override
    public void onItemClick(String mapId) {
        playRoomViewModel.setPoint(mapId);
    }
}