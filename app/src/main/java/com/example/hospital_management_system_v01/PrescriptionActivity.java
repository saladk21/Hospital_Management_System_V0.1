package com.example.hospital_management_system_v01;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PrescriptionActivity extends BaseActivity {

    private Database myDB;
    private Spinner patientSpinner, medicineSpinner;
    private ArrayList<String> patientNames, medicineNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        // Initialize the database
        myDB = new Database(PrescriptionActivity.this);

        // Initialize UI elements
        patientSpinner = findViewById(R.id.patientSpinner);
        medicineSpinner = findViewById(R.id.medicineSpinner);
        Button prescribeButton = findViewById(R.id.prescribeButton);

        // Populate spinners with patient and medicine data
        loadPatientData();
        loadMedicineData();

        // Set up adapters for spinners
        ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientNames);
        ArrayAdapter<String> medicineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicineNames);

        // Set drop-down style
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply adapters to spinners
        patientSpinner.setAdapter(patientAdapter);
        medicineSpinner.setAdapter(medicineAdapter);

        prescribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected patient and medicine IDs
                long patientId = getSelectedPatientId();
                long medicineId = getSelectedMedicineId();

                // Check if a patient and medicine are selected
                if (patientId != -1 && medicineId != -1) {
                    // Prescribe the selected medicine to the patient
                    myDB.prescribeMedicine(patientId, medicineId);

                    // Optionally, you can show a toast or other UI feedback to indicate success
                    // Example: Toast.makeText(PrescriptionActivity.this, "Medicine prescribed successfully", Toast.LENGTH_SHORT).show();

                    // Finish the activity or navigate back to the Doctor activity
                    finish();
                } else {
                    // Show an error message if patient or medicine is not selected
                    Toast.makeText(PrescriptionActivity.this, "Please select a patient and a medicine", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPatientData() {
        // Retrieve patient data from the database
        Cursor cursor = myDB.getAllRecords(Database.PATIENT_TABLE_NAME);
        try {
            if (cursor.getCount() > 0) {
                patientNames = new ArrayList<>();
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") long patientId = cursor.getLong(cursor.getColumnIndex(Database.COLUMN_ID));
                    @SuppressLint("Range") String patientName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                    patientNames.add(patientName);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void loadMedicineData() {
        // Retrieve medicine data from the database
        Cursor cursor = myDB.getAllRecords(Database.MEDICINE_TABLE_NAME);
        try {
            if (cursor.getCount() > 0) {
                medicineNames = new ArrayList<>();
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") long medicineId = cursor.getLong(cursor.getColumnIndex(Database.COLUMN_ID));
                    @SuppressLint("Range") String medicineName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                    medicineNames.add(medicineName);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    @SuppressLint("Range")
    private long getSelectedPatientId() {
        // Get the selected patient's ID from the spinner
        int position = patientSpinner.getSelectedItemPosition();
        if (position != Spinner.INVALID_POSITION) {
            Cursor cursor = myDB.getAllRecords(Database.PATIENT_TABLE_NAME);
            try {
                if (cursor.moveToPosition(position)) {
                    return cursor.getLong(cursor.getColumnIndex(Database.COLUMN_ID));
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
        return -1;
    }

    @SuppressLint("Range")
    private long getSelectedMedicineId() {
        // Get the selected medicine's ID from the spinner
        int position = medicineSpinner.getSelectedItemPosition();
        if (position != Spinner.INVALID_POSITION) {
            Cursor cursor = myDB.getAllRecords(Database.MEDICINE_TABLE_NAME);
            try {
                if (cursor.moveToPosition(position)) {
                    return cursor.getLong(cursor.getColumnIndex(Database.COLUMN_ID));
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
        return -1;
    }

}
