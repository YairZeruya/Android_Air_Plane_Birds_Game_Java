package com.example.hw1.Objects;

import com.google.android.gms.location.FusedLocationProviderClient;

public class Record {
    private String rank;
    private String score;
    private double latitude;
    private double longitude;


    public Record(String rank, String score, double latitude, double longitude) {
        this.rank = rank;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRank() {
        return rank;
    }

    public String getScore() {
        return score;
    }


    @Override
    public String toString() {
        return "Record{" +
                "rank=" + rank +
                ", score=" + score +
                '}';
    }
}
