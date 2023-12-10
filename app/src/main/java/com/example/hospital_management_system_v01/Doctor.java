package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Doctor extends BaseActivity {


    RecyclerView recyclerView;
    RecyclerView recyclerView2;

    ConstraintLayout constraintLayout;
    ConstraintLayout constraintLayout2;
    Database myDB;
    ArrayList<String> columnID, pName, pID, docID, dateList, timeList;
    ArrayList<String> patientID, patientName, patientAge, patientGender, patientIllness, patStatues;
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

        myDB = new Database(Doctor.this);
        columnID = new ArrayList<>();
        pName = new ArrayList<>();
        pID = new ArrayList<>();
        docID = new ArrayList<>();
        dateList = new ArrayList<>();
        timeList = new ArrayList<>();

        Button showApp = findViewById(R.id.showApp);
        Button showPatients = findViewById(R.id.showPatients);
        Button btnPrescribe = findViewById(R.id.btnPrescribe);


        patientName = new ArrayList<>();
        patientGender = new ArrayList<>();
        patientAge = new ArrayList<>();
        patientID = new ArrayList<>();
        patientIllness = new ArrayList<>();
        patStatues = new ArrayList<>();



        StoreDataInArrays();
        StoreDataInArrays2();

        customAdapter = new CustomAdapter(Doctor.this, columnID, pName, pID, dateList, timeList);
        customAdapter2 = new CustomAdapter2(Doctor.this, patientName, patientGender, patientAge, patientID, patientIllness ,patStatues);
        recyclerView.setAdapter(customAdapter);
        recyclerView2.setAdapter(customAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(Doctor.this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(Doctor.this));

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

        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPrescriptionActivity("PRESCRIBE_MEDICINE");
            }
        });

    }

    @SuppressLint("Range")
    void StoreDataInArrays() {
        Cursor cursor = myDB.readAllData();
        try {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "no data.", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    columnID.add(cursor.getString(0));
                    pName.add(cursor.getString(cursor.getColumnIndex(Database.APPOINTMENT_COLUMN_PATIENT_NAME)));
                    pID.add(cursor.getString(cursor.getColumnIndex(Database.APPOINTMENT_COLUMN_PATIENT_ID)));
                    dateList.add(cursor.getString(cursor.getColumnIndex(Database.APPOINTMENT_COLUMN_DATE)));
                    timeList.add(cursor.getString(cursor.getColumnIndex(Database.APPOINTMENT_COLUMN_TIME)));
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    @SuppressLint("Range")
    void StoreDataInArrays2() {
        Cursor cursor = myDB.readAllData2();
        try {
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "no data.", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    patientName.add(cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME)));
                    patientGender.add(cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_GENDER)));
                    patientAge.add(cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_AGE)));
                    patientID.add(cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_ID)));
                    patientIllness.add(cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_ILLNESS)));
                    patStatues.add(cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_STATUS)));
                }
            }
        } finally {
            // Ensure the cursor is closed in the finally block
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void launchPrescriptionActivity(String actionType) {
        Intent intent = new Intent(Doctor.this, PrescriptionActivity.class);
        intent.putExtra("ACTION_TYPE", actionType);
        startActivity(intent);
    }

}