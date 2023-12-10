package com.example.hospital_management_system_v01;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class Admin extends BaseActivity {

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

        userTypeLayout = findViewById(R.id.userTypeLayout);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);

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
            @SuppressLint("Range") String userName = auditTrailCursor.getString(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_USER));
            @SuppressLint("Range") String action = auditTrailCursor.getString(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_ACTION));
            @SuppressLint("Range") long timestamp = auditTrailCursor.getLong(auditTrailCursor.getColumnIndex(Database.AUDIT_TRAIL_COLUMN_TIMESTAMP));

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
        // Add this line to set up the initial visibility
        userTypeLayout.setVisibility(View.VISIBLE);

        // Create a dialog to input user information based on user type
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select User Type");

        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        // Find the Spinner in the dialog layout
        Spinner userTypeSpinnerDialog = dialogView.findViewById(R.id.userTypeSpinnerDialog);

        // Populate the Spinner with user types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getUserTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinnerDialog.setAdapter(adapter);

        // Set up the buttons
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hide the userTypeLayout when user has selected a type
                userTypeLayout.setVisibility(View.GONE);

                // Get the selected user type from the Spinner
                String userType = userTypeSpinnerDialog.getSelectedItem().toString();

                // Continue with the user type-specific logic (inflate layout, etc.)
                addSpecificUser(userType);

                // Handle user type-specific logic based on the selected user type
                int layoutId = getLayoutId(userType);
                View userSpecificDialogView = getLayoutInflater().inflate(layoutId, null);

                // Continue with your logic to handle the specific user type dialog
                // For example, show another AlertDialog or customize the current one based on the user type
                // You can access the userSpecificDialogView and its components to perform specific actions

                // Retrieve the entered information based on user type
                ContentValues values = getUserTypeValues(userSpecificDialogView, userType);

                // Add the record to the database
                long result = database.addRecord(userType, values);
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
                return -1; // Return an invalid layout ID or handle accordingly
        }
    }

    // populate the user type Spinner
    private List<String> getUserTypes() {
        List<String> userTypes = new ArrayList<>();
        userTypes.add(Database.ADMIN_TABLE_NAME);
        userTypes.add(Database.DOCTOR_TABLE_NAME);
        userTypes.add(Database.NURSE_TABLE_NAME);
        userTypes.add(Database.PATIENT_TABLE_NAME);
        userTypes.add(Database.MEDICINE_TABLE_NAME);
        return userTypes;
    }

    // Get the ContentValues based on the user type and entered information
    private ContentValues getUserTypeValues(View dialogView, String userType) {
        ContentValues values = new ContentValues();

        // Retrieve additional fields based on user type
        switch (userType) {
            case Database.ADMIN_TABLE_NAME:
                String adminName = ((EditText) dialogView.findViewById(R.id.editTextAdminName)).getText().toString();
                String adminPassword = ((EditText) dialogView.findViewById(R.id.editTextAdminPassword)).getText().toString();

                values.put(Database.COLUMN_NAME, adminName);
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
                EditText ageEditText = dialogView.findViewById(R.id.editTextAge);
                String ageString = ageEditText.getText().toString().trim();

                // Check if the age string is not empty before parsing
                if (!ageString.isEmpty()) {
                    int patientAge = Integer.parseInt(ageString);
                    values.put(Database.PATIENT_COLUMN_AGE, patientAge);
                } else {
                    // Handle the case where age is empty, e.g., show an error or set a default value
                    // For now, let's assume a default age of 0
                    values.put(Database.PATIENT_COLUMN_AGE, 0);
                }

                String patientGender = ((EditText) dialogView.findViewById(R.id.editTextGender)).getText().toString();
                String patientName = ((EditText) dialogView.findViewById(R.id.editTextPatientName)).getText().toString();
                String patientPass = ((EditText) dialogView.findViewById(R.id.editTextPassword)).getText().toString();
                String patientIll = ((EditText) dialogView.findViewById(R.id.editTextIllness)).getText().toString();
                String patientStat = ((EditText) dialogView.findViewById(R.id.editTextSeverity)).getText().toString();

                values.put(Database.PATIENT_COLUMN_GENDER, patientGender);
                values.put(Database.COLUMN_NAME, patientName);
                values.put(Database.COLUMN_PASSWORD, patientPass);
                values.put(Database.PATIENT_COLUMN_ILLNESS, patientIll);
                values.put(Database.PATIENT_COLUMN_STATUS, patientStat);

                break;

            case Database.MEDICINE_TABLE_NAME:
                String medicineName = ((EditText) dialogView.findViewById(R.id.editTextMedicineName)).getText().toString();
                String medicineEx = ((EditText) dialogView.findViewById(R.id.editTextExpiryDate)).getText().toString();

                // Check if the medicine stock quantity is not empty before parsing
                EditText stockEditText = dialogView.findViewById(R.id.editTextStockQuantity);
                String stockString = stockEditText.getText().toString().trim();

                if (!stockString.isEmpty()) {
                    int medicineStock = Integer.parseInt(stockString);
                    values.put(Database.MEDICINE_COLUMN_STOCK, medicineStock);
                } else {
                    // Handle the case where stock quantity is empty, e.g., show an error or set a default value
                    // For now, let's assume a default stock quantity of 0
                    values.put(Database.MEDICINE_COLUMN_STOCK, 0);
                }

                values.put(Database.COLUMN_NAME, medicineName);
                values.put(Database.MEDICINE_COLUMN_EXPIRY_DATE, medicineEx);

                break;
            default:
                // Handle unknown user types or provide a default behavior
                break;

        }

        return values;
    }

    private void addSpecificUser(String userType) {
        Log.d("AddSpecificUser", "Adding " + userType);

        // Inflate the corresponding layout based on the selected user type
        AlertDialog.Builder specificUserBuilder = new AlertDialog.Builder(this);
        specificUserBuilder.setTitle("Add " + userType);

        // Inflate the layout for the specific user type
        View userSpecificDialogView = getLayoutInflater().inflate(getLayoutId(userType), null);
        specificUserBuilder.setView(userSpecificDialogView);

        specificUserBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("AddSpecificUser", "Save button clicked");

                // Retrieve the entered information based on user type
                ContentValues values = getUserTypeValues(userSpecificDialogView, userType);

                // Add the record to the database
                try {
                    long result = database.addRecord(userType, values);
                    Log.d("AddSpecificUser", "Record added to the database. Result: " + result);
                } catch (Exception e) {
                    Log.e("AddSpecificUser", "Error adding record to the database", e);
                    e.printStackTrace();
                }

                // Show a toast based on the result
                Toast.makeText(Admin.this, "User added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        specificUserBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        specificUserBuilder.create().show();
    }





    private void editUser() {
        // Show a dialog to select the user type to edit
        AlertDialog.Builder selectUserBuilder = new AlertDialog.Builder(this);
        selectUserBuilder.setTitle("Select User Type");

        // Inflate the dialog layout
        View selectUserDialogView = getLayoutInflater().inflate(R.layout.dialog_select_user, null);
        selectUserBuilder.setView(selectUserDialogView);

        // Find the Spinner in the dialog layout
        Spinner selectUserTypeSpinner = selectUserDialogView.findViewById(R.id.selectUserTypeSpinner);

        // Populate the Spinner with user types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getUserTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectUserTypeSpinner.setAdapter(adapter);

        // Set up the buttons for selecting a user type
        selectUserBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected user type from the Spinner
                String selectedUserType = selectUserTypeSpinner.getSelectedItem().toString();

                // Show the list of existing items for the selected user type
                showExistingItemsForEdit(selectedUserType);
            }
        });

        selectUserBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectUserBuilder.create().show();
    }



    // Add this method for editing a specific user
    private void editSpecificUser(String userType, long itemId) {
        // Fetch the user details based on the user type and item ID
        Cursor userCursor = database.getRecordById(userType, itemId);

        if (userCursor != null && userCursor.moveToFirst()) {
            // Inflate the dialog layout based on the user type
            int layoutId = getLayoutId(userType);
            if (layoutId == -1) {
                // Handle invalid layout ID
                Toast.makeText(Admin.this, "Invalid user type", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder editUserBuilder = new AlertDialog.Builder(this);
            editUserBuilder.setTitle("Edit " + userType);

            View dialogView = getLayoutInflater().inflate(layoutId, null);
            editUserBuilder.setView(dialogView);

            // Retrieve the user details from the cursor and populate the dialog fields
            getUserTypeValuesFromCursor(dialogView, userType, userCursor);

            // Set up the buttons for editing a user
            editUserBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Retrieve the entered information based on user type
                    ContentValues updatedValues = getUserTypeValues(dialogView, userType);

                    // Update the record in the database
                    int result = database.updateRecord(userType, itemId, updatedValues);

                    // Show a toast based on the result
                    if (result > 0) {
                        Toast.makeText(Admin.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Admin.this, "Error updating user", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            editUserBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            editUserBuilder.create().show();

            // Close the cursor
            userCursor.close();
        } else {
            // Handle the case where the cursor is null or empty
            Toast.makeText(Admin.this, "User details not found", Toast.LENGTH_SHORT).show();
        }
    }





    // Helper method to retrieve values from the cursor and populate the dialog fields
    @SuppressLint("Range")
    private void getUserTypeValuesFromCursor(View dialogView, String userType, Cursor cursor) {
        // Retrieve additional fields based on user type from the cursor
        switch (userType) {
            case Database.ADMIN_TABLE_NAME:
                String adminName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                String adminPassword = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PASSWORD));
                // Populate the dialog fields
                ((EditText) dialogView.findViewById(R.id.editTextAdminName)).setText(adminName);
                ((EditText) dialogView.findViewById(R.id.editTextAdminPassword)).setText(adminPassword);
                break;
            case Database.DOCTOR_TABLE_NAME:
                String doctorName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                String doctorSpecialty = cursor.getString(cursor.getColumnIndex(Database.DOCTOR_COLUMN_SPECIALTY));
                String doctorPass = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PASSWORD));
                // Populate the dialog fields
                ((EditText) dialogView.findViewById(R.id.editTextDoctorName)).setText(doctorName);
                ((EditText) dialogView.findViewById(R.id.editTextSpecialization)).setText(doctorSpecialty);
                ((EditText) dialogView.findViewById(R.id.editTextPassword)).setText(doctorPass);
                break;
            case Database.NURSE_TABLE_NAME:
                String nurseName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                String nurseDepartment = cursor.getString(cursor.getColumnIndex(Database.NURSE_COLUMN_DEPARTMENT));
                String nursePass = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PASSWORD));
                // Populate the dialog fields
                ((EditText) dialogView.findViewById(R.id.editTextNurseName)).setText(nurseName);
                ((EditText) dialogView.findViewById(R.id.editTextDepartment)).setText(nurseDepartment);
                ((EditText) dialogView.findViewById(R.id.editTextPassword)).setText(nursePass);
                break;
            case Database.PATIENT_TABLE_NAME:
                String patientName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                String patientPass = cursor.getString(cursor.getColumnIndex(Database.COLUMN_PASSWORD));
                int patientAge = cursor.getInt(cursor.getColumnIndex(Database.PATIENT_COLUMN_AGE));
                String patientGender = cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_GENDER));
                String patientIllness = cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_ILLNESS));
                String patientStatus = cursor.getString(cursor.getColumnIndex(Database.PATIENT_COLUMN_STATUS));
                // Populate the dialog fields
                ((EditText) dialogView.findViewById(R.id.editTextPatientName)).setText(patientName);
                ((EditText) dialogView.findViewById(R.id.editTextPassword)).setText(patientPass);
                ((EditText) dialogView.findViewById(R.id.editTextAge)).setText(String.valueOf(patientAge));
                ((EditText) dialogView.findViewById(R.id.editTextGender)).setText(patientGender);
                ((EditText) dialogView.findViewById(R.id.editTextIllness)).setText(patientIllness);
                ((EditText) dialogView.findViewById(R.id.editTextSeverity)).setText(patientStatus);
                break;
            case Database.MEDICINE_TABLE_NAME:
                String medicineName = cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME));
                String medicineExpiryDate = cursor.getString(cursor.getColumnIndex(Database.MEDICINE_COLUMN_EXPIRY_DATE));
                int medicineStock = cursor.getInt(cursor.getColumnIndex(Database.MEDICINE_COLUMN_STOCK));
                // Populate the dialog fields
                ((EditText) dialogView.findViewById(R.id.editTextMedicineName)).setText(medicineName);
                ((EditText) dialogView.findViewById(R.id.editTextExpiryDate)).setText(medicineExpiryDate);
                ((EditText) dialogView.findViewById(R.id.editTextStockQuantity)).setText(String.valueOf(medicineStock));
                break;
        }
    }







    private void showExistingItemsForEdit(String userType) {
        // Get a cursor containing existing items for the selected user type
        Cursor existingItemsCursor = database.getAllRecords(userType);

        // Check if there are existing items
        if (existingItemsCursor != null && existingItemsCursor.getCount() > 0) {
            // Create a list to store existing item names
            List<String> existingItemsList = new ArrayList<>();

            // Extract existing item names from the cursor
            while (existingItemsCursor.moveToNext()) {
                @SuppressLint("Range") String itemName = existingItemsCursor.getString(existingItemsCursor.getColumnIndex(Database.COLUMN_NAME));
                existingItemsList.add(itemName);
            }

            // Close the cursor
            existingItemsCursor.close();

            // Show a dialog with a list of existing items for the user to choose
            AlertDialog.Builder chooseItemBuilder = new AlertDialog.Builder(this);
            chooseItemBuilder.setTitle("Select Item to Edit");

            // Convert the list to an array for the dialog
            final String[] existingItemsArray = existingItemsList.toArray(new String[0]);

            // Set up the list of existing items
            chooseItemBuilder.setItems(existingItemsArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the selected existing item name
                    String selectedItemName = existingItemsArray[which];

                    // Retrieve the user ID or other unique identifier for the selected item
                    long itemId = getItemId(userType, selectedItemName);

                    // Continue with the user type-specific logic (show edit dialog, etc.)
                    editSpecificUser(userType, itemId);
                }
            });

            chooseItemBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            chooseItemBuilder.create().show();
        } else {
            Toast.makeText(this, "No existing items for editing.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the item ID based on the item name and user type
    @SuppressLint("Range")
    private long getItemId(String userType, String itemName) {
        Cursor recordCursor = database.getAllRecords(userType);

        long itemId = -1;

        // Check if there are records
        if (recordCursor != null && recordCursor.getCount() > 0) {
            // Find the record with the matching item name
            while (recordCursor.moveToNext()) {
                String currentName = recordCursor.getString(recordCursor.getColumnIndex(Database.COLUMN_NAME));
                if (itemName.equals(currentName)) {
                    // Found the matching record, retrieve its ID
                    itemId = recordCursor.getLong(recordCursor.getColumnIndex(getTableIdColumn(userType)));
                    break;
                }
            }

            // Close the cursor
            recordCursor.close();
        }

        return itemId;
    }

    private String getTableIdColumn(String tableName) {
        switch (tableName) {
            case Database.DOCTOR_TABLE_NAME:
                return Database.DOCTOR_COLUMN_ID;
            case Database.NURSE_TABLE_NAME:
                return Database.NURSE_COLUMN_ID;
            case Database.PATIENT_TABLE_NAME:
                return Database.PATIENT_COLUMN_ID;
            case Database.MEDICINE_TABLE_NAME:
                return Database.MEDICINE_COLUMN_ID;
            case Database.ADMIN_TABLE_NAME:
                return Database.ADMIN_COLUMN_ID;
            default:
                return Database.COLUMN_ID;
        }
    }

    private void deleteUser() {
        // Show a dialog to select the user type to delete
        AlertDialog.Builder selectUserBuilder = new AlertDialog.Builder(this);
        selectUserBuilder.setTitle("Select User Type");

        // Inflate the dialog layout
        View selectUserDialogView = getLayoutInflater().inflate(R.layout.dialog_select_user, null);
        selectUserBuilder.setView(selectUserDialogView);

        // Find the Spinner in the dialog layout
        Spinner selectUserTypeSpinner = selectUserDialogView.findViewById(R.id.selectUserTypeSpinner);

        // Populate the Spinner with user types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getUserTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectUserTypeSpinner.setAdapter(adapter);

        // Set up the buttons for selecting a user type
        selectUserBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected user type from the Spinner
                String selectedUserType = selectUserTypeSpinner.getSelectedItem().toString();

                // Show the list of existing items for the selected user type
                showExistingItemsForDelete(selectedUserType);
            }
        });

        selectUserBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        selectUserBuilder.create().show();
    }

    // Add this method for deleting a specific user
    private void deleteSpecificUser(String userType, long itemId) {
        // Show a confirmation dialog before deleting the user
        AlertDialog.Builder confirmDeleteBuilder = new AlertDialog.Builder(this);
        confirmDeleteBuilder.setTitle("Confirm Deletion");
        confirmDeleteBuilder.setMessage("Are you sure you want to delete this user?");

        // Set up the buttons for confirming or canceling the deletion
        confirmDeleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the record from the database
                int result = database.deleteRecord(userType, itemId);

                // Show a toast based on the result
                if (result > 0) {
                    Toast.makeText(Admin.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin.this, "Error deleting user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmDeleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        confirmDeleteBuilder.create().show();
    }

    private void showExistingItemsForDelete(String userType) {
        // Get a cursor containing existing items for the selected user type
        Cursor existingItemsCursor = database.getAllRecords(userType);

        // Check if there are existing items
        if (existingItemsCursor != null && existingItemsCursor.getCount() > 0) {
            // Create a list to store existing item names
            List<String> existingItemsList = new ArrayList<>();

            // Extract existing item names from the cursor
            while (existingItemsCursor.moveToNext()) {
                @SuppressLint("Range") String itemName = existingItemsCursor.getString(existingItemsCursor.getColumnIndex(Database.COLUMN_NAME));
                existingItemsList.add(itemName);
            }

            // Close the cursor
            existingItemsCursor.close();

            // Show a dialog with a list of existing items for the user to choose
            AlertDialog.Builder chooseItemBuilder = new AlertDialog.Builder(this);
            chooseItemBuilder.setTitle("Select Item to Delete");

            // Convert the list to an array for the dialog
            final String[] existingItemsArray = existingItemsList.toArray(new String[0]);

            // Set up the list of existing items
            chooseItemBuilder.setItems(existingItemsArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the selected existing item name
                    String selectedItemName = existingItemsArray[which];

                    // Retrieve the user ID or other unique identifier for the selected item
                    long itemId = getItemId(userType, selectedItemName);

                    // Continue with the user type-specific logic (show delete confirmation, etc.)
                    deleteSpecificUser(userType, itemId);
                }
            });

            chooseItemBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            chooseItemBuilder.create().show();
        } else {
            Toast.makeText(this, "No existing items for deletion.", Toast.LENGTH_SHORT).show();
        }
    }

}
