package com.example.hw1.Activities;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Interfaces.RecordCallBack;
import com.example.hw1.Fragments.RecordFragment;
import com.example.hw1.Fragments.MapFragment;
import com.example.hw1.R;
import com.example.hw1.Utilities.SignalGenerator;

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
        SignalGenerator.getInstance().toast("Press on a record to view its location.", Toast.LENGTH_LONG);
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
