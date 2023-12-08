package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class Doctor extends AppCompatActivity {

    Database myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        //initialize database
        myDB = new Database(Doctor.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_id = new ArrayList<>();

    }

    void StoreDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this,"no data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }
    }
}