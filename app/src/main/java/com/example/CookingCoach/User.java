package com.example.CookingCoach;

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
}
