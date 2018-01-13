package com.example.nurgali.gps_tracker.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.R;

public class SettingsActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_FINE_LOCATION_REQUEST = 101;
    private boolean permissionIsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_FINE_LOCATION_REQUEST:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionIsGranted = true;
                } else{
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "Бағдарламаның жұмысына рұқсат беру керек", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void btnPermission (View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION_REQUEST);
        } else {
            permissionIsGranted = true;
        }
    }

    public void btnBackToMap (View view) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        finish();
        Toast.makeText(this,"Картаға қайта оралдыңыз", Toast.LENGTH_LONG).show();
    }

}
