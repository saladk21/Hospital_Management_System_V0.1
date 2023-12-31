package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button by its ID
        Button adminButton = findViewById(R.id.adminButton);
        Button patButton = findViewById(R.id.patButton);
        Button doctorButton = findViewById(R.id.doctorButton);
        Button loginButton = findViewById(R.id.loginButton);
        Button nurseButton = findViewById(R.id.nurseButton);


        // Set up a click event for the button
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Admin activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
            }
        });
        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Admin activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, Doctor.class);
                startActivity(intent);
            }
        });
        patButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Open the Admin activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, Patient.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Login activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        nurseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the Admin activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, Nurse.class);
                startActivity(intent);
            }

        });

    }

}
