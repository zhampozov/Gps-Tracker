package com.example.nurgali.gps_tracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.R;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void btnBackToMap (View view) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        finish();
        Toast.makeText(this,"Картаға қайта оралдыңыз", Toast.LENGTH_LONG).show();
    }

}
