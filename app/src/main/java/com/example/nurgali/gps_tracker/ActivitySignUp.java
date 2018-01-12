package com.example.nurgali.gps_tracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etEmail, etPassword, etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

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


       mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(ActivitySignUp.this, "Сіз тіркелдіңіз", Toast.LENGTH_LONG).show();
                           finish();
                           startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       } else {
                           Toast.makeText(ActivitySignUp.this, "Тіркелуді қайтадан бастаңыз", Toast.LENGTH_LONG).show();
                       }
                   }
               });
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
