package com.example.hw1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.Logic.DataManager;
import com.example.hw1.Logic.GameManager;
import com.example.hw1.MySPv;
import com.example.hw1.R;

public class RecordFragment extends Fragment {

    private RecyclerView main_LST_records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.records_fragment, container, false);
        findViews(view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        RecordAdapter recordAdapter = new RecordAdapter(DataManager.getRecords());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_records.setAdapter(recordAdapter);
        main_LST_records.setLayoutManager(linearLayoutManager);
    }

    private void findViews(View view) {
        main_LST_records = view.findViewById(R.id.main_LST_records);
    }
}
