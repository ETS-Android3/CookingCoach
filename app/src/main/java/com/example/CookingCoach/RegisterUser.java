package com.example.CookingCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            //if clicked on banner we got back to home page
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            //when the button is pressed to register user
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        //convert what the user inputted into string
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        //if full name is empty ask for full name until it is satisfied
        if(fullName.isEmpty())
        {
            editTextFullName.setError("Please Enter Full Name");
            editTextFullName.requestFocus();
            return;
        }
        //same thing for age
        if(age.isEmpty())
        {
            editTextAge.setError("Please Enter Age");
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            editTextEmail.setError("Please Enter Email Address");
            editTextEmail.requestFocus();
            return;
        }
        //if email it not valid (does not contain @ or .com or other stuff)
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please Enter Valid Email Address");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("Please Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        //check if password if less than 6 characters or not
        if(password.length()<6)
        {
            editTextPassword.setError("Please Enter Password longer than 6 Characters");
            editTextPassword.requestFocus();
            return;
        }
        //set visibility of progress bar to true
        progressBar.setVisibility(View.VISIBLE);

        //use firebase method to authenticate
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check if the user has been registered
                        if(task.isSuccessful())
                        {
                            User user = new User(fullName,age,email);
                            //send the information to the data base
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this,"User Has Been Registered",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterUser.this,"Failed To Register User, Try Again",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this,"Failed To Register User, Try Again",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}