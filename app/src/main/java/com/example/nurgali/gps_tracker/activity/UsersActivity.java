package com.example.nurgali.gps_tracker.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.R;
import com.example.nurgali.gps_tracker.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
/*
    private DatabaseReference userlistReference;
    private ValueEventListener mUserListListener;
    ArrayList<String> usernameList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.users_activity);
        userlistReference = FirebaseDatabase.getInstance().getReference().child("users").child("username");
        onStart();
        userListView = (ListView) findViewById(R.id.userlistview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernameList = new ArrayList<>((ArrayList) dataSnapshot.getValue());
                usernameList.remove(usernameOfCurrentUser());
                arrayAdapter = new ArrayAdapter(UsersActivity.this, android.R.layout.simple_list_item_1, usernameList);
                userListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UsersActivity.this, "Ne poluchilos'", Toast.LENGTH_LONG).show();
            }
        };
        userlistReference.addValueEventListener(userListener);
            mUserListListener = userListener;
    }

    public String usernameOfCurrentUser() {
        String email = ActivitySignIn.mAuth.getCurrentUser().getEmail();
        if (email.contains("@")) {
            return email.split("@") [0];
        } else {
            return email;
        }
    }*/
}
