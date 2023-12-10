package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Nurse extends BaseActivity {


    private Database database; // Declare the Database object
    private Spinner doctorSpinner;
    private Spinner patientSpinner;
    private Spinner medicineSpinner;
    private EditText dateText;
    private EditText timeText;
    private EditText newMedicineName;
    private EditText newMedicineStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);
        Button assignDoctor = findViewById(R.id.assignButton);
        Button bookAppointment = findViewById(R.id.bookAppointmentButton);
        // Initialize the Database object
        database = new Database(this);
        doctorSpinner = findViewById(R.id.doctorSpinner);
        patientSpinner = findViewById(R.id.patientSpinner);
        medicineSpinner = findViewById(R.id.medicineSpinner);
        dateText = findViewById(R.id.dateEditText);
        timeText = findViewById(R.id.timeEditText);
        newMedicineName = findViewById(R.id.newMedicineName);
        newMedicineStock = findViewById(R.id.newMedicineStock);
        Button editMedicineButton = findViewById(R.id.editMedicineButton);
        displayAllMedicines();
        populateDoctorSpinner();
        populatePatientSpinner();
        populateMedicineSpinner();



        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignButtonClickBook(v);

            }
        });
        assignDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignButtonClick(v);
            }
        });

        editMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to populate the medicine spinner
                onAssignButtonClickMedicine(v);

            }
        });

        // Get the medicine information for a specific medicine ID (you need to define medicineId)

    }

    // Function to get and display medicine information based on medicine ID
// Inside Nurse class

    // Function to display all available medicines
    private void displayAllMedicines() {
        Cursor medicineCursor = database.getAllRecords(Database.MEDICINE_TABLE_NAME);
        TableLayout medicineTableLayout = findViewById(R.id.medicineTableLayout);

        if (medicineCursor != null && medicineCursor.moveToFirst()) {
            do {
                // Retrieve medicine details from the cursor
                int medicineIdColumnIndex = medicineCursor.getColumnIndex(Database.MEDICINE_COLUMN_ID);
                int medicineNameColumnIndex = medicineCursor.getColumnIndex(Database.COLUMN_NAME);
                int stockColumnIndex = medicineCursor.getColumnIndex(Database.MEDICINE_COLUMN_STOCK);

                int medicineId = medicineCursor.getInt(medicineIdColumnIndex);
                String medicineName = medicineCursor.getString(medicineNameColumnIndex);
                int stock = medicineCursor.getInt(stockColumnIndex);

                // Create a new TableRow
                TableRow row = new TableRow(this);

                // Create TextViews to hold medicine information
                TextView medicineIdTextView = new TextView(this);
                TextView medicineNameTextView = new TextView(this);
                TextView stockTextView = new TextView(this);

                // Set text for TextViews
                medicineIdTextView.setText(String.valueOf(medicineId));
                medicineNameTextView.setText(medicineName);
                stockTextView.setText(String.valueOf(stock));

                // Add TextViews to the TableRow
                row.addView(medicineIdTextView);
                row.addView(medicineNameTextView);
                row.addView(stockTextView);

                // Add the TableRow to the TableLayout
                medicineTableLayout.addView(row);

            } while (medicineCursor.moveToNext());
        }
    }
        // Display the medicine list in the TextView or handle accordingly

    public int getSelectedMedicineId(){
        // Ensure medicineSpinner.getSelectedItem() is not null before parsing to int
        if (medicineSpinner.getSelectedItem() != null) {
            return Integer.parseInt(medicineSpinner.getSelectedItem().toString().split(":")[0].trim());
        }
        return -1; // Handle the case when nothing is selected
    }
    private String getNewMedicineName() {
        // Retrieve text from EditText for new medicine name
        return newMedicineName.getText().toString().trim();
    }

    public int getNewMedicineStock(){
        // Retrieve text from EditText for new medicine stock
        return Integer.parseInt(newMedicineStock.getText().toString().trim());
    }

    private void populateMedicineSpinner() {
        // Fetch medicine data from the database
        Cursor medicineCursor = database.getAllRecords(Database.MEDICINE_TABLE_NAME);

        // Create an ArrayList to store medicine names or IDs
        ArrayList<String> medicineList = new ArrayList<>();

        if (medicineCursor != null && medicineCursor.moveToFirst()) {
            do {
                // Get medicine details from the cursor
                @SuppressLint("Range") int medicineId = medicineCursor.getInt(medicineCursor.getColumnIndex(Database.MEDICINE_COLUMN_ID));
                @SuppressLint("Range") String medicineName = medicineCursor.getString(medicineCursor.getColumnIndex(Database.COLUMN_NAME));

                // Append medicine ID and name to the list
                medicineList.add(medicineId + ": " + medicineName);
            } while (medicineCursor.moveToNext());
        }

        // Close the cursor to release resources
        if (medicineCursor != null) {
            medicineCursor.close();
        }

        // Create an ArrayAdapter and set it to the Medicine Spinner
        ArrayAdapter<String> medicineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicineList);
        medicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicineSpinner.setAdapter(medicineAdapter);
    }

    public void onAssignButtonClickMedicine(View view) {

        int medicineId = getSelectedMedicineId();
        String newMedicineName = getNewMedicineName();
        int newMedicineStock = getNewMedicineStock();


        database.updateMedicine(medicineId, newMedicineName,newMedicineStock);
    }



    public int getSelectedDoctorId(){

      if (doctorSpinner.getSelectedItem() != null) {
        return Integer.parseInt(doctorSpinner.getSelectedItem().toString().split(":")[0].trim());
    }
        return -1; // Handle the case when nothing is selected
}
    public int getSelectedPatientId(){
       if (patientSpinner.getSelectedItem() != null) {
        return Integer.parseInt(patientSpinner.getSelectedItem().toString().split(":")[0].trim());
    }
        return -1; // Handle the case when nothing is selected
}



    public void onAssignButtonClick(View view) {
        // Get selected doctor and patient IDs from spinners
        int doctorId = getSelectedDoctorId();
        int patientId = getSelectedPatientId();

        // Assign the patient to the doctor
        database.assignDoctorToPatient(patientId, doctorId);
    }
    private void populateDoctorSpinner() {
        // Fetch doctors' data from the database
        Cursor doctorCursor = database.getAllRows(Database.DOCTOR_TABLE_NAME);

        // Create an ArrayList to store doctor names or IDs
        ArrayList<String> doctorList = new ArrayList<>();

        if (doctorCursor != null && doctorCursor.moveToFirst()) {
            do {
                // Assuming doctor's name is stored in the 'name' column
                @SuppressLint("Range") String doctorName = doctorCursor.getString(doctorCursor.getColumnIndex(Database.COLUMN_NAME));
                @SuppressLint("Range") int doctorId = doctorCursor.getInt(doctorCursor.getColumnIndex(Database.DOCTOR_COLUMN_ID));

                doctorList.add(doctorId+ ": " +doctorName);
            } while (doctorCursor.moveToNext());
        }

        // Close the cursor to release resources
        if (doctorCursor != null) {
            doctorCursor.close();
        }

        // Create an ArrayAdapter and set it to the Doctor Spinner
        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(doctorAdapter);
    }

    private void populatePatientSpinner() {
        // Fetch patients' data from the database
        Cursor patientCursor = database.getAllRows(Database.PATIENT_TABLE_NAME);

        // Create an ArrayList to store patient names or IDs
        ArrayList<String> patientList = new ArrayList<>();

        if (patientCursor != null && patientCursor.moveToFirst()) {
            do {
                // Assuming patient's name is stored in the 'name' column
                @SuppressLint("Range") String patientName = patientCursor.getString(patientCursor.getColumnIndex(Database.COLUMN_NAME));
                @SuppressLint("Range") int patientId = patientCursor.getInt(patientCursor.getColumnIndex(Database.PATIENT_COLUMN_ID));

                patientList.add(patientId +": "+ patientName);
            } while (patientCursor.moveToNext());
        }

        // Close the cursor to release resources
        if (patientCursor != null) {
            patientCursor.close();
        }

        // Create an ArrayAdapter and set it to the Patient Spinner
        ArrayAdapter<String> patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientList);
        patientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSpinner.setAdapter(patientAdapter);
    }


    // Other initialization or operations as needed



    // Other methods and functionalities as needed
    public void onAssignButtonClickBook(View view) {

        int doctorId = getSelectedDoctorId();
        int patientId = getSelectedPatientId();
        String date = getDate();
        String time = getTime();

        // Assign the patient to the doctor
        database.bookAppointment(doctorId,patientId,date,time);
    }

    private String  getTime() {
        return timeText.getText().toString().trim();
    }

    private String getDate() {
        return dateText.getText().toString().trim();
    }



}
