package com.example.nurgali.gps_tracker;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nurgali on 22.05.2017.
 */

public class User {
    public String username;
    public String uid;
    public String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }
}