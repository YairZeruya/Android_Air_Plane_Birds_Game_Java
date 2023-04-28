package com.example.hw1;

import java.util.ArrayList;

public class RecordsList {
    private ArrayList<Record> records = new ArrayList<Record>();

    public RecordsList() {
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
}
