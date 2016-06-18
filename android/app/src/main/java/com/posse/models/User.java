package com.posse.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String fbid;
    public String firstName;
    public String lastName;
    public String gender;

    public User() {
    }

    public User(String fbid, String firstName, String lastName, String gender) {
        this.fbid = fbid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
}
