package com.example.hospital_management_system_v01;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Patient extends BaseActivity {

    private TextView doctorInfoTextView;
    private TextView medicineInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        doctorInfoTextView = findViewById(R.id.doctorInfoTextView);
        medicineInfoTextView = findViewById(R.id.medicineInfoTextView);

        // Get the patient's information from the database
        Database database = new Database(this);

        // Get the username from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // Get the Patient ID associated with the current user
        int patientId = getPatientIdByUsername(username);

        TextView bannerTextView = findViewById(R.id.bannerTextView);
        bannerTextView.setText("Patient Page - " + username);

        // Fetch the patient's information based on the Patient ID
        Cursor patientCursor = database.getRecordById(Database.PATIENT_TABLE_NAME, patientId);

        if (patientCursor.moveToFirst()) {
            int doctorIdColumnIndex = patientCursor.getColumnIndex("assigned_to");
            int medicineIdColumnIndex = patientCursor.getColumnIndex("prescribed_with");

            if (doctorIdColumnIndex != -1 && medicineIdColumnIndex != -1) {
                long doctorId = patientCursor.getLong(doctorIdColumnIndex);
                long medicineId = patientCursor.getLong(medicineIdColumnIndex);

                // Fetch and display the assigned doctor's information
                Cursor doctorCursor = database.getRecordById(Database.DOCTOR_TABLE_NAME, doctorId);
                if (doctorCursor.moveToFirst()) {
                    int doctorNameColumnIndex = doctorCursor.getColumnIndex(Database.COLUMN_NAME);
                    if (doctorNameColumnIndex != -1) {
                        String doctorName = doctorCursor.getString(doctorNameColumnIndex);
                        doctorInfoTextView.setText("Assigned Doctor: " + doctorName);
                    } else {
                        doctorInfoTextView.setText("Assigned Doctor: Not Available");
                    }
                } else {
                    doctorInfoTextView.setText("Assigned Doctor: Not Available");
                }

                // Fetch and display the prescribed medicine's information
                Cursor medicineCursor = database.getRecordById(Database.MEDICINE_TABLE_NAME, medicineId);
                if (medicineCursor.moveToFirst()) {
                    int medicineNameColumnIndex = medicineCursor.getColumnIndex(Database.COLUMN_NAME);
                    if (medicineNameColumnIndex != -1) {
                        String medicineName = medicineCursor.getString(medicineNameColumnIndex);
                        medicineInfoTextView.setText("Prescribed Medicine: " + medicineName);
                    } else {
                        medicineInfoTextView.setText("Prescribed Medicine: Not Available");
                    }
                } else {
                    medicineInfoTextView.setText("Prescribed Medicine: Not Available");
                }
            } else {
                doctorInfoTextView.setText("Assigned Doctor: Not Available");
                medicineInfoTextView.setText("Prescribed Medicine: Not Available");
            }
        }

        // Close the cursors
        patientCursor.close();
        database.close();
    }

    private void logout() {
        finish();
        Intent intent = new Intent(Patient.this, Login.class);
        startActivity(intent);
    }

    // Helper method to get the Patient ID based on the username
    private int getPatientIdByUsername(String username) {
        Database database = new Database(this);
        int patientId = database.getPatientIdByUsername(username);
        database.close();
        return patientId;
    }
}
