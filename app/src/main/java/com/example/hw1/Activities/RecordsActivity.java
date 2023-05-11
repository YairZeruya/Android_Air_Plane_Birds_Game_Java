package com.example.hw1.Activities;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Interfaces.RecordCallBack;
import com.example.hw1.Fragments.RecordFragment;
import com.example.hw1.Fragments.MapFragment;
import com.example.hw1.R;

public class RecordsActivity extends AppCompatActivity {
    private RecordFragment recordsFragment;
    private MapFragment mapFragment;
    private RecordCallBack recordCallBack = new RecordCallBack() {
        @Override
        public void recordClicked(double latitude, double longitude) {
            mapFragment.zoomOnRecord(latitude, longitude);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records_screen);
        initFragments();
        recordsFragment.setRecordCallBack(recordCallBack);
        beginTransactions();
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_records, recordsFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }

    private void initFragments() {
        recordsFragment = new RecordFragment();
        mapFragment = new MapFragment();
    }

}
