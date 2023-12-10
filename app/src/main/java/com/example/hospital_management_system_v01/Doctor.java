package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Doctor extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView recyclerView2;

    ConstraintLayout constraintLayout;
    ConstraintLayout constraintLayout2;
    Database myDB;
    ArrayList<String> columnID, pName, pID, docID;
    ArrayList<String> patientID, patientName, patientAge, patientGender, patientIllness, patientMed;
    CustomAdapter customAdapter;
    CustomAdapter2 customAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Log.d("DoctorActivity", "Doctor activity created");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2= findViewById(R.id.recyclerView2);
        constraintLayout = findViewById(R.id.showAppLayout);
        constraintLayout2 = findViewById(R.id.showPatientsLayout);

        //initialize database

        //button 1
        myDB = new Database(Doctor.this);
        columnID = new ArrayList<>();
        pName = new ArrayList<>();
        pID = new ArrayList<>();
        docID = new ArrayList<>();
        Button showApp = findViewById(R.id.showApp);

        //button 2
        patientName = new ArrayList<>();
        patientGender = new ArrayList<>();
        patientAge = new ArrayList<>();
        patientID = new ArrayList<>();
        patientIllness = new ArrayList<>();
        patientMed = new ArrayList<>();
        Button showPatients = findViewById(R.id.showPatients);


        StoreDataInArrays();
        StoreDataInArrays2();

        customAdapter = new CustomAdapter(Doctor.this, columnID, pName, pID, docID);
        customAdapter2 = new CustomAdapter2(Doctor.this, patientAge, patientGender, patientAge, patientID, patientIllness, patientMed);
        recyclerView.setAdapter(customAdapter);
        recyclerView2.setAdapter(customAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(Doctor.this));

        showApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewDoc) {
                // set layout visibility to visible
                int visibility = constraintLayout.getVisibility();
                if (visibility == 8) {
                    constraintLayout.setVisibility(View.VISIBLE);

                } else {
                    constraintLayout.setVisibility(View.GONE);
                }
            }
        });
        showPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewDoc) {
                // set layout visibility to visible
                int visibility = constraintLayout2.getVisibility();
                if (visibility == 8) {
                    constraintLayout2.setVisibility(View.VISIBLE);

                } else {
                    constraintLayout2.setVisibility(View.GONE);
                }
            }
        });

    }

    void StoreDataInArrays() {
        Cursor cursor = myDB.readAllData();
        try {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "no data.", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    columnID.add(cursor.getString(0));
                    pName.add(cursor.getString(1));
                    pID.add(cursor.getString(2));
                    docID.add(cursor.getString(3));
                }
                Log.d("DoctorActivity", "columnID size: " + columnID.size());
                Log.d("DoctorActivity", "pName size: " + pName.size());
                Log.d("DoctorActivity", "pID size: " + pID.size());
                Log.d("DoctorActivity", "docID size: " + docID.size());
            }
        } finally {
            // Ensure the cursor is closed in the finally block
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
    void StoreDataInArrays2() {
        Cursor cursor = myDB.readAllData2();
        try {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "no data.", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    patientName.add(cursor.getString(0));
                    patientGender.add(cursor.getString(1));
                    patientAge.add(cursor.getString(2));
                    patientID.add(cursor.getString(3));
                    patientIllness.add(cursor.getString(4));
                    patientMed.add(cursor.getString(5));
                }
            }
        } finally {
            // Ensure the cursor is closed in the finally block
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

}