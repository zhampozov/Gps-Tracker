package com.example.nurgali.gps_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.activity.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mAuth.getCurrentUser()!= null){
            startActivity(new Intent(this, MapsActivity.class));
            Toast.makeText(this, "Здравствуйте " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Вы не авторизованы", Toast.LENGTH_SHORT).show();
        }

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivitySignIn.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivitySignUp.class));
                finish();
            }
        });
    }
}
