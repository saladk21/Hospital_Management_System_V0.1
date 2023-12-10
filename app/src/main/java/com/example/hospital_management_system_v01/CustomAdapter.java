package com.example.hospital_management_system_v01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList columnID, pName, pID, dateList, timeList;

    CustomAdapter(Context context, ArrayList columnID, ArrayList pName, ArrayList pID, ArrayList dateList, ArrayList timeList) {
        this.context = context;
        this.columnID = columnID;
        this.pName = pName;
        this.pID = pID;
        this.dateList = dateList;
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.columnID_txt.setText(String.valueOf(columnID.get(position)));
        holder.pName_txt.setText(String.valueOf(pName.get(position)));
        holder.pID_txt.setText(String.valueOf(pID.get(position)));
        holder.date_txt.setText(String.valueOf(dateList.get(position)));
        holder.time_txt.setText(String.valueOf(timeList.get(position)));
    }



    @Override
    public int getItemCount() {
        return columnID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView columnID_txt, pName_txt, pID_txt, date_txt, time_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            columnID_txt = itemView.findViewById(R.id.Column_id_txt);
            pName_txt = itemView.findViewById(R.id.patName_txt);
            pID_txt = itemView.findViewById(R.id.patID_txt);
            date_txt = itemView.findViewById(R.id.appDate);
            time_txt = itemView.findViewById(R.id.appTime_txt);
        }

    }
}

