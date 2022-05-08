package com.example.CookingCoach;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    //string for age and full name
    public String fullName, age, email;

    //create a empty constructor
    //this is to access the arguments
    public User()
    {

    }
    //another constructor to for the input
    public User(String fullName, String age, String email)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
    }

    /*
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if(fullName != null)
            result.put("fullName", fullName);

        if(email != null)
            result.put("email", email);

        if(age != null)
            result.put("uriImage", age);

        return result;
    }
    */

}
