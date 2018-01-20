package com.example.nurgali.gps_tracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.BaseActivity;
import com.example.nurgali.gps_tracker.R;
import com.example.nurgali.gps_tracker.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivitySignUp extends BaseActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseUsers, userEmails;
    private EditText etEmail, etPassword, etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        userEmails = FirebaseDatabase.getInstance().getReference();

        Button btnBack = (Button) findViewById(R.id.btnBack);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirm = (EditText) findViewById(R.id.etConfirm);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void signUp (final String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(ActivitySignUp.this, "Жолақтарды толтыруыңыз керек", Toast.LENGTH_LONG).show();
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(ActivitySignUp.this, "Сіз тіркелдіңіз", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ActivitySignUp.this, "Тіркелуді қайтадан бастаңыз", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(ActivitySignUp.this, MainActivity.class));
        finish();
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email, userId);

        databaseUsers.child("users").child(userId).setValue(user);
        userEmails.child("userEmails").child(usernameFromEmail(email)).child("uniqueId").setValue(userId);
    }

    public void onClickSignUp (View v) {
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (password.equals(confirm)){
            signUp(etEmail.getText().toString(), password);
        } else {
            Toast.makeText(ActivitySignUp.this, "Құпия сөз сәйкес емес", Toast.LENGTH_LONG).show();
            etPassword.setText("");
            etConfirm.setText("");
        }
        }

}
