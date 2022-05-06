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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPasword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        //initialize sign in button
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        //initialize editText for email and password
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPasword = (EditText) findViewById(R.id.password);

        //initialize progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //initialize progress bar
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                //if the user clicks on the sign in button
                //we will create that method needed
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;


        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPasword.getText().toString().trim();

        //if the user entered password
        if(email.isEmpty())
        {
            editTextEmail.setError("Email is Needed");
            editTextEmail.requestFocus();
            return;
        }
        //if the user provided valid email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please Enter Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        //if password is empty
        if(password.isEmpty())
        {
            editTextPasword.setError("Please Enter Password");
            editTextPasword.requestFocus();
            return;
        }
        //check password length
        if(password.length()<6)
        {
            editTextPasword.setError("Password length must be more than 5 characters");
            editTextPasword.requestFocus();
            return;
        }
        //set the progress bar on until they get to sign in
        progressBar.setVisibility(View.VISIBLE);

        //sign in with email and password
        //add on complete listener is showing if the task has been completed
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(MainActivity.this, ProfilePage.class));
                    /*if (user.isEmailVerified()) {
                        //redirect to user profile
                        //now we redirect the user to the profile page
                        startActivity(new Intent(MainActivity.this, ProfilePage.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email for verification.", Toast.LENGTH_LONG).show();
                    }*/

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Failed To Login. Please Check Credintials",Toast.LENGTH_LONG).show();

                }
                }
            }
        );

    }
}