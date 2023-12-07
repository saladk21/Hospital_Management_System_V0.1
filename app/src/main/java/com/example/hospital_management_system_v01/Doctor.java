package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Doctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // BLANK

        Database database;

        //initialize
        database = new Database(this);

        // View appointments
        database.getAllRecords(Database.APPOINTMENT_TABLE_NAME);

        // change patient status ie case severe or cured
        // Message the nurse with the prescription using intents.

    }
}