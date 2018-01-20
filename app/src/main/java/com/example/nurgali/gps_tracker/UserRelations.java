package com.example.nurgali.gps_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nurgali.gps_tracker.activity.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRelations extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("contacts-to");
    ListView listView;
    final ArrayList<String> list = new ArrayList<String>();
    public static String EXTRA_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_relations);
        listView = (ListView) findViewById(R.id.userRelations);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);


        mData.child(currentUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                           Log.d("CON TO", dataSnapshot1.getValue().toString());
                           for (DataSnapshot d : dataSnapshot1.getChildren()){
                               User user = d.getValue(User.class);
                               System.out.println("/*/*//////////////////////: " + user.getEmail());
                               list.add(user.getEmail());
                               EXTRA_MESSAGE = user.getUid();
                           }
                           if (!list.isEmpty()){
                               listView.setAdapter(adapter);
                           } else {
                               Toast.makeText(getApplicationContext(), "У вас нет друзей", Toast.LENGTH_SHORT).show();
                           }
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
    }
}
