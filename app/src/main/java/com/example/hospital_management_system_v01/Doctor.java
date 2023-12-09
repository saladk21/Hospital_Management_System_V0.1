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
    ConstraintLayout constraintLayout;
    FloatingActionButton add_button;
    Database myDB;
    ArrayList<String> columnID, pName, pID, docID;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Log.d("DoctorActivity", "Doctor activity created");

        recyclerView = findViewById(R.id.recyclerView);
        constraintLayout = findViewById(R.id.showAppLayout);

        //initialize database
        myDB = new Database(Doctor.this);
        columnID = new ArrayList<>();
        pName = new ArrayList<>();
        pID = new ArrayList<>();
        docID = new ArrayList<>();
        Button showApp = findViewById(R.id.showApp);

        StoreDataInArrays();

        customAdapter = new CustomAdapter(Doctor.this, columnID, pName, pID, docID);
        recyclerView.setAdapter(customAdapter);
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

    }

    void StoreDataInArrays() {
        Cursor cursor = myDB.readAllData();
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

    }
}