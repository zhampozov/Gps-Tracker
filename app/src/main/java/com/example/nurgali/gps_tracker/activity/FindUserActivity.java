package com.example.nurgali.gps_tracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.R;
import com.example.nurgali.gps_tracker.User;
import com.example.nurgali.gps_tracker.activity.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {
    String userEmail = "";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ListView listView;
    public static String EXTRA_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        listView = (ListView) findViewById(R.id.userlistview);
        Button btnFind = (Button) findViewById(R.id.searchUser);
        final EditText email = (EditText) findViewById(R.id.emailToFind);


        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                findUserById(userEmail);
            }
        });

    }

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    final ArrayList<String> list = new ArrayList<String>();

    public void findUserById(final String email) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        databaseReference.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            User user = data.getValue(User.class);
                            list.add(user.getEmail());
                            if (user.getEmail().equals(email)){
                                if (currentUser.getEmail().equals(email)){
                                    Toast.makeText(getApplicationContext(), "Please write correct email", Toast.LENGTH_SHORT).show();
                                }else {
                                    flag = true;
                                    EXTRA_MESSAGE = user.getUid();
                                    databaseReference.child("contacts-to").child(currentUser.getUid()).child(user.getUid()).child("to").setValue(user);
                                    databaseReference.child("contacts-for").child(user.getUid()).child(currentUser.getUid()).child("for").setValue(currentUser);
                                    System.out.println("yahoooooooooooooooooooooooo!");
                                    listView.setAdapter(adapter);
                                }
                            }
                            break;
                        }
                        if(!flag){
                            Toast.makeText(getApplicationContext(), "Пользователь не найден!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Error", "getUser:onCancelled", databaseError.toException());
                    }
                }

        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
               intent.putExtra(EXTRA_MESSAGE ,EXTRA_MESSAGE);
               startActivity(intent);
            }
        });

    };


}
