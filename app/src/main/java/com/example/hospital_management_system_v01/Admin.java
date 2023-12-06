package com.example.hospital_management_system_v01;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Admin extends AppCompatActivity {

    private LinearLayout dashboardSection;
    private Button addUserButton, editUserButton, deleteUserButton;
    private TextView recentAuditTrailEventsTextView;

    private LinearLayout userTypeLayout;
    private Spinner userTypeSpinner;

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dashboardSection = findViewById(R.id.dashboardSection);
        recentAuditTrailEventsTextView = findViewById(R.id.recentAuditTrailEventsTextView);

        addUserButton = findViewById(R.id.buttonAddUser);
        editUserButton = findViewById(R.id.buttonEditUser);
        deleteUserButton = findViewById(R.id.buttonDeleteUser);
        Button dashboardButton = findViewById(R.id.dashboardButton);

        // Initialize database
        database = new Database(this);

        // Set up click listeners
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDashboard();
            }
        });
    }

    private void showDashboard() {
        // Update the TextViews with actual values from the database
        int numberOfPatients = database.getNumberOfRecords(Database.PATIENT_TABLE_NAME);
        int numberOfAppointments = database.getNumberOfRecords(Database.APPOINTMENT_TABLE_NAME);

        // Find the TextViews in the layout
        TextView numberOfPatientsTextView = findViewById(R.id.numberOfPatientsTextView);
        TextView numberOfAppointmentsTextView = findViewById(R.id.numberOfAppointmentsTextView);

        // Set the actual values to the TextViews
        numberOfPatientsTextView.setText("Number of Patients: " + numberOfPatients);
        numberOfAppointmentsTextView.setText("Number of Appointments: " + numberOfAppointments);

        // Toggle the visibility of the dashboard section
        if (dashboardSection.getVisibility() == View.VISIBLE) {
            dashboardSection.setVisibility(View.GONE);
        } else {
            dashboardSection.setVisibility(View.VISIBLE);
        }
    }

    private void viewAuditTrail() {
        // Update the TextView with actual audit trail events from the database
        Cursor auditTrailCursor = database.getAuditTrail();

        StringBuilder auditTrailStringBuilder = new StringBuilder();

        while (auditTrailCursor.moveToNext()) {
            String userName = auditTrailCursor.getString(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_USER));
            String action = auditTrailCursor.getString(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_ACTION));
            long timestamp = auditTrailCursor.getLong(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_TIMESTAMP));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date(timestamp));

            auditTrailStringBuilder.append("User: ").append(userName)
                    .append("\nAction: ").append(action)
                    .append("\nTimestamp: ").append(formattedDate)
                    .append("\n\n");
        }

        auditTrailCursor.close();

        // Display audit trail in the TextView
        recentAuditTrailEventsTextView.setText(auditTrailStringBuilder.toString());
        Toast.makeText(this, "Audit Trail Loaded", Toast.LENGTH_SHORT).show();
    }

    private void addUser() {
        // Get the selected user type
        String userType = userTypeSpinner.getSelectedItem().toString();

        // Create a dialog to input user information based on user type
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add " + userType);

        // Inflate the corresponding layout based on the selected user type
        View dialogView = getLayoutInflater().inflate(getLayoutId(userType), null);
        builder.setView(dialogView);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the entered information based on user type
                ContentValues values = getUserTypeValues(dialogView, userType);

                // Add the record to the database
                long result = database.addRecord(userType, values);

                // Show a toast based on the result
                if (result != -1) {
                    Toast.makeText(Admin.this, "User added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin.this, "Error adding user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    // Get the layout resource ID based on the user type
    private int getLayoutId(String userType) {
        switch (userType) {
            case Database.ADMIN_TABLE_NAME:
                return R.layout.layout_admin_fields;
            case Database.DOCTOR_TABLE_NAME:
                return R.layout.layout_doctor_fields;
            case Database.NURSE_TABLE_NAME:
                return R.layout.layout_nurse_fields;
            case Database.PATIENT_TABLE_NAME:
                return R.layout.layout_patient_fields;
            case Database.MEDICINE_TABLE_NAME:
                return R.layout.layout_medicine_fields;
            default:
                // Return a default layout or handle the case as needed
                return R.layout.layout_admin_fields;
        }
    }

    // Get the ContentValues based on the user type and entered information
    private ContentValues getUserTypeValues(View dialogView, String userType) {
        ContentValues values = new ContentValues();

        // Retrieve additional fields based on user type
        switch (userType) {
            case Database.ADMIN_TABLE_NAME:
                String adminName = ((EditText) dialogView.findViewById(R.id.editTextAdminName)).getText().toString();
                String adminPassword = ((EditText) dialogView.findViewById(R.id.editTextAdminPassword)).getText().toString();
                values.put(Database.ADMIN_COLUMN_USERNAME, adminName);
                values.put(Database.COLUMN_PASSWORD, adminPassword);
                break;
            case Database.DOCTOR_TABLE_NAME:
                String doctorName = ((EditText) dialogView.findViewById(R.id.editTextDoctorName)).getText().toString();
                String doctorSpecialty = ((EditText) dialogView.findViewById(R.id.editTextSpecialization)).getText().toString();
                String doctorPass = ((EditText) dialogView.findViewById(R.id.editTextPassword)).getText().toString();
                values.put(Database.COLUMN_NAME, doctorName);
                values.put(Database.COLUMN_PASSWORD, doctorPass);
                values.put(Database.DOCTOR_COLUMN_SPECIALTY, doctorSpecialty);
                break;
            case Database.NURSE_TABLE_NAME:
                String nurseName = ((EditText) dialogView.findViewById(R.id.editTextNurseName)).getText().toString();
                String nurseDepartment = ((EditText) dialogView.findViewById(R.id.editTextDepartment)).getText().toString();
                String nursePass = ((EditText) dialogView.findViewById(R.id.editTextPassword)).getText().toString();
                values.put(Database.COLUMN_NAME, nurseName);
                values.put(Database.NURSE_COLUMN_DEPARTMENT, nurseDepartment);
                values.put(Database.COLUMN_PASSWORD, nursePass);
                break;
            case Database.PATIENT_TABLE_NAME:
                int patientAge = Integer.parseInt(((EditText) dialogView.findViewById(R.id.editTextAge)).getText().toString());
                String patientGender = ((EditText) dialogView.findViewById(R.id.editTextGender)).getText().toString();
                String patientname = ((EditText) dialogView.findViewById(R.id.editTextPatientName)).getText().toString();
                String patientpass = ((EditText) dialogView.findViewById(R.id.editTextPassword)).getText().toString();
                String patientIll = ((EditText) dialogView.findViewById(R.id.editTextIllness)).getText().toString();
                String patientStat = ((EditText) dialogView.findViewById(R.id.editTextSeverity)).getText().toString();
                values.put(Database.PATIENT_COLUMN_AGE, patientAge);
                values.put(Database.PATIENT_COLUMN_GENDER, patientGender);
                values.put(Database.COLUMN_NAME, patientname);
                values.put(Database.COLUMN_PASSWORD, patientpass);
                values.put(Database.PATIENT_COLUMN_ILLNESS, patientIll);
                values.put(Database.PATIENT_COLUMN_STATUS, patientStat);
                break;
            case Database.MEDICINE_TABLE_NAME:
                String medicineName = ((EditText) dialogView.findViewById(R.id.editTextMedicineName)).getText().toString();
                String medicineEx = ((EditText) dialogView.findViewById(R.id.editTextExpiryDate)).getText().toString();
                int medicineStock = Integer.parseInt(((EditText) dialogView.findViewById(R.id.editTextStockQuantity)).getText().toString());
                values.put(Database.COLUMN_NAME, medicineStock);
                values.put(Database.MEDICINE_COLUMN_EXPIRY_DATE, medicineEx);
                values.put(Database.MEDICINE_COLUMN_STOCK, medicineStock);
                break;
            default:
                // Handle unknown user types or provide a default behavior
                break;
        }

        return values;
    }


    private void editUser() {
        long userId = getUserId(); // Get the user ID to update

        if (userId != -1) {
            String updatedUserName = userNameEditText.getText().toString().trim();

            if (!updatedUserName.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(Database.COLUMN_NAME, updatedUserName);

                String selectedUserType = userTypeSpinner.getSelectedItem().toString();

                int result = database.updateRecord(selectedUserType, userId, values);

                if (result > 0) {
                    Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error updating user", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a valid user name", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where user ID is not valid
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser() {
        long userId = getUserId(); // Get the user ID to delete

        if (userId != -1) {
            String selectedUserType = userTypeSpinner.getSelectedItem().toString();

            int result = database.deleteRecord(selectedUserType, userId);

            if (result > 0) {
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error deleting user", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where user ID is not valid
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the user ID from the selected item in the database
    private long getUserId() {
        // Retrieve the selected item's ID from the database using your logic
        // For example, if you have a Spinner to select a user, get the user ID from the selected item.

        // Replace the following line with your actual logic to get the user ID
        return -1; // Return a placeholder value for now
    }
}
