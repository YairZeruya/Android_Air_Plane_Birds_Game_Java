package com.example.hw1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.Interfaces.RecordCallBack;
import com.example.hw1.R;
import com.example.hw1.Objects.Record;

import java.util.ArrayList;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordsViewHolder> {

    private ArrayList<Record> records;
    private RecordCallBack recordCallBack;

    public void setRecordCallBack(RecordCallBack recordCallBack) {
        this.recordCallBack = recordCallBack;
    }

    public RecordAdapter(ArrayList<Record> records, RecordCallBack recordCallBack) {
        this.records = records;
        this.recordCallBack = recordCallBack;
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
        holder.record_layout.setOnClickListener(v -> recordClicked(record.getLatitude(), record.getLongitude()));
    }

    private void recordClicked(double latitude, double longitude) {
        if (recordCallBack != null) {
            recordCallBack.recordClicked(latitude, longitude);
        }
    }

    @Override
    public int getItemCount() {
        return this.records == null ? 0 : this.records.size();
    }

    public Record getItem(int position) {
        return this.records.get(position);
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {
        private TextView record_LBL_rank;
        private TextView record_LBL_score;
        private LinearLayout record_layout;


        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_rank = itemView.findViewById(R.id.rank_textview);
            record_LBL_score = itemView.findViewById(R.id.score_textview);
            record_layout = itemView.findViewById(R.id.record_layout);
        }
    }
}
