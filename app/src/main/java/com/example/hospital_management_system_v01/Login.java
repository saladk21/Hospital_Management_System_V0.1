package com.example.hospital_management_system_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText username,password;
    Button login;
    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.Username);
        password=findViewById(R.id.Password);
        login=findViewById(R.id.Login);
        db=new Database(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String COLUMN_NAME =username.getText().toString();
                String COLUMN_PASSWORD=password.getText().toString();
                if (TextUtils.isEmpty(COLUMN_NAME)|| TextUtils.isEmpty(COLUMN_PASSWORD))
                    Toast.makeText(Login.this,"Please Enter the required field",Toast.LENGTH_SHORT).show();
                else{
                    Boolean checking=db.checkInfo(COLUMN_NAME,COLUMN_PASSWORD);
                    if (checking==true){
                        Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        //The below part is related of what we discussed regarding the user type , start new activity based on user type.
                       // if (role != null && !role.isEmpty()) {
                            // Perform actions based on the role (e.g., navigate to different screens)
//                            switch (role) {
//                                case "Nurse":
//                                    // User is a Nurse
//                                    break;
//                                case "Patient":
//                                    // User is a Patient
//                                    break;
//                                case "Doctor":
//                                    // User is a Doctor
//                                    break;
//                                case "Admin":
//                                    // User is an Admin
//                                    break;
//                                default:
//                                    // Role not recognized
//                                    break;
//                            }
//                        } else {
//                            // Username or password is incorrect
//                        }
//                    } else {
//                        // Username or password is incorrect
//                    }

                    }else{

                        Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();

                    }


                }
            }
        });


    }


}


