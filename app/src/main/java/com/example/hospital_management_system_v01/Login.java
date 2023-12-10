package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseActivity {
    EditText username, password;
    Button login;
    Database db;

    // Declare class-level variables
    String COLUMN_NAME;
    String COLUMN_PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        db = new Database(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move these lines inside the onClick method
                COLUMN_NAME = username.getText().toString().trim();
                COLUMN_PASSWORD = password.getText().toString().trim();

                // Check if the username and password are not empty
                if (TextUtils.isEmpty(COLUMN_NAME) || TextUtils.isEmpty(COLUMN_PASSWORD)) {
                    Toast.makeText(Login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor;

                // Check login for each user type
                if ((cursor = db.checkLogin(Database.ADMIN_TABLE_NAME, COLUMN_NAME, COLUMN_PASSWORD)).moveToFirst()) {
                    // Valid credentials for admin
                    openUserTypeActivity("admin");
                } else if ((cursor = db.checkLogin(Database.DOCTOR_TABLE_NAME, COLUMN_NAME, COLUMN_PASSWORD)).moveToFirst()) {
                    // Valid credentials for doctor
                    openUserTypeActivity("doctor");
                } else if ((cursor = db.checkLogin(Database.NURSE_TABLE_NAME, COLUMN_NAME, COLUMN_PASSWORD)).moveToFirst()) {
                    // Valid credentials for nurse
                    openUserTypeActivity("nurse");
                } else if ((cursor = db.checkLogin(Database.PATIENT_TABLE_NAME, COLUMN_NAME, COLUMN_PASSWORD)).moveToFirst()) {
                    // Valid credentials for patient
                    openUserTypeActivity("patient");
                } else {
                    // Invalid credentials
                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

                // Close the cursor
                if (cursor != null) {
                    cursor.close();
                }
            }

            private void openUserTypeActivity(String userType) {
                Intent intent;

                switch (userType) {
                    case "admin":
                        intent = new Intent(Login.this, Admin.class);
                        break;
                    case "doctor":
                        intent = new Intent(Login.this, Doctor.class);
                        break;
                    case "nurse":
                        intent = new Intent(Login.this, Nurse.class);
                        break;
                    case "patient":
                        intent = new Intent(Login.this, Patient.class);
                        break;
                    default:
                        // Handle unexpected user type
                        return;
                }

                // Pass the username to the next activity if needed
                intent.putExtra("USERNAME", COLUMN_NAME);

                startActivity(intent);
                finish(); // Close the login activity
            }
        });
    }
}
