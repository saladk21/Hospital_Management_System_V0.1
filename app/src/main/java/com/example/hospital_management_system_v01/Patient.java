package com.example.hospital_management_system_v01;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Patient extends AppCompatActivity {

    private TextView doctorInfoTextView;
    private TextView medicineInfoTextView;
    private TextView patientNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        doctorInfoTextView = findViewById(R.id.doctorInfoTextView);
        medicineInfoTextView = findViewById(R.id.medicineInfoTextView);
        patientNameTextView = findViewById(R.id.patientNameTextView);

        // Get the patient's information from the database
        Database database = new Database(this);

        // Retrieve patient's name from the intent
        String patientName = getIntent().getStringExtra("USERNAME");

        // Display patient's name
        patientNameTextView.setText("Patient: " + patientName);

        Cursor patientCursor = database.getAllRecords(Database.PATIENT_TABLE_NAME);

        if (patientCursor.moveToFirst()) {
            int doctorIdColumnIndex = patientCursor.getColumnIndex("assigned_to");
            int medicineIdColumnIndex = patientCursor.getColumnIndex("prescribed_with");

            if (doctorIdColumnIndex != -1 && medicineIdColumnIndex != -1) {
                long doctorId = patientCursor.getLong(doctorIdColumnIndex);
                long medicineId = patientCursor.getLong(medicineIdColumnIndex);

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
}
