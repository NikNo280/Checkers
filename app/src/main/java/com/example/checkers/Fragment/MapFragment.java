package com.example.checkers.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkers.Adapter.MapAdapter;
import com.example.checkers.R;
import com.example.checkers.ViewModel.PlayRoomViewModel;

import java.util.Objects;

public class MapFragment extends Fragment implements MapAdapter.ItemListener{

    protected String[] nameList = new String[64];
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    PlayRoomViewModel playRoomViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playRoomViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PlayRoomViewModel.class);
        playRoomViewModel.getMap().observe(Objects.requireNonNull(requireActivity()), s -> {
            MapAdapter mapAdapter = new MapAdapter(s, nameList, this);
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
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                nameList[i * 8 + j] = String.valueOf((i * 8) + j);
            }

        return view;
    }

    @Override
    public void onItemClick(String mapId) {
        playRoomViewModel.setPoint(mapId);
    }
}