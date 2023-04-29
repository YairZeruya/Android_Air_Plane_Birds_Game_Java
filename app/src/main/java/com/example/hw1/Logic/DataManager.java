package com.example.hw1.Logic;

import com.example.hw1.Utilities.MySPv;
import com.example.hw1.Objects.Record;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Record> getRecords() {

        ArrayList records = new ArrayList();
        int fromSP = MySPv.getInstance().getInt("Num Of Records", 0);
        for (int i = 0; i < fromSP; i++) {
            String s = MySPv.getInstance().getString("Rank: " + (i + 1), "");
            if (s != "") {
                Record record = new Gson().fromJson(s, Record.class);
                records.add(record);
            }
        }
        return records;
    }
}
