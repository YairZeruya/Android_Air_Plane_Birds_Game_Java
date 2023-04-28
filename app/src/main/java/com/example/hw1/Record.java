package com.example.hw1;

public class Record {
    private String rank;
    private String score;

    public Record(String rank, String score) {
        this.rank = rank;
        this.score = score;
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
