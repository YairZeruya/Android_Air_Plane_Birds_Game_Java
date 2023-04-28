package com.example.hw1.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.R;
import com.example.hw1.Record;

import java.util.ArrayList;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordsViewHolder> {

    private ArrayList<Record> records;

    public RecordAdapter(ArrayList<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        RecordsViewHolder recordsViewHolder = new RecordsViewHolder(view);
        return recordsViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordsViewHolder holder, int position) {
        Record record = getItem(position);
        holder.record_LBL_rank.setText(record.getRank());
        holder.record_LBL_score.setText(record.getScore());
    }

    @Override
    public int getItemCount() {
        return this.records == null ? 0 : this.records.size();
    }

    private Record getItem(int position) {
        return this.records.get(position);
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {
        private TextView record_LBL_rank;
        private TextView record_LBL_score;


        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_rank = itemView.findViewById(R.id.rank_textview);
            record_LBL_score = itemView.findViewById(R.id.score_textview);
        }
    }
}
