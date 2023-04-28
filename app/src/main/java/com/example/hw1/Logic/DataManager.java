package com.example.hw1.Logic;

import com.example.hw1.MySPv;
import com.example.hw1.Record;
import com.example.hw1.RecordsList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Record> getRecords() {
        ArrayList records = new ArrayList();
        for (int i = 0; i < 10; i++) {
            String s = MySPv.getInstance().getString("Rank: " + (i + 1), "");
            if (s != "") {
                Record record = new Gson().fromJson(s, Record.class);
                records.add(record);
            }
        }
        return records;
    }
}
