package com.example.nurgali.gps_tracker.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.BaseActivity;
import com.example.nurgali.gps_tracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySignIn extends BaseActivity{

    public static FirebaseAuth mAuth;
    private static final String TAG = "ActivitySignIn";

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void signIn() {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(ActivitySignIn.this, "Жүйеге кіру сәтті аяқталды", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(ActivitySignIn.this, "Қайталап көріңіз", Toast.LENGTH_LONG).show();
                            etPassword.setText("");
                        }
                    }
                });
    }

    public void onClickEnter(View v) {
        signIn();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            result = false;
        } else {
            etEmail.setError(null);
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            result = false;
        } else {
            etPassword.setError(null);
        }

        return result;
    }
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Go to MainActivity
        startActivity(new Intent(ActivitySignIn.this, MapsActivity.class));
        finish();
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }

}
}