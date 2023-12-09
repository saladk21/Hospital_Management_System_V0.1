package com.example.hospital_management_system_v01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {

    private Context context;
    private ArrayList patientID, patientName, patientAge, patientGender, patientIllness, patientMed;

    CustomAdapter2(Context context, ArrayList patientName, ArrayList patientGender, ArrayList patientAge, ArrayList patientID, ArrayList patientIllness, ArrayList patientMed) {
        this.context = context;
        this.patientName = patientName;
        this.patientGender = patientGender;
        this.patientAge = patientAge;
        this.patientID = patientID;
        this.patientIllness= patientIllness;
        this.patientMed = patientMed;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row2, parent, false);
                return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.patientName_txt.setText(String.valueOf(patientName.get(position)));
        holder.patientGender_txt.setText(String.valueOf(patientGender.get(position)));
        holder.patientAge_txt.setText(String.valueOf(patientAge.get(position)));
        holder.patientID_txt.setText(String.valueOf(patientID.get(position)));
        holder.patientIllness_txt.setText(String.valueOf(patientIllness.get(position)));
        holder.patientMed_txt.setText(String.valueOf(patientMed.get(position)));
    }

    @Override
    public int getItemCount() {
        return patientID.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientName_txt, patientGender_txt, patientAge_txt, patientID_txt, patientIllness_txt, patientMed_txt;;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName_txt = itemView.findViewById(R.id.patName_txt);
            patientGender_txt =itemView.findViewById(R.id.patGender_txt);
            patientAge_txt =itemView.findViewById(R.id.patAge_txt);
            patientID_txt =itemView.findViewById(R.id.patID_txt);
            patientIllness_txt =itemView.findViewById(R.id.patIllness_txt);
            patientMed_txt = itemView.findViewById(R.id.patMed_txt);
        }
    }
}
