package com.example.CookingCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.PutDocumentsRequest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    EditText profileName, profileEmail, profileAge;
    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    //DocumentReference documentReference;
   // FirebaseFirestore db;
    private Button updateProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


       Intent data = getIntent();
       String fullName = data.getStringExtra("fullName");
       String email = data.getStringExtra("email");
       String age = data.getStringExtra("age");
        auth = FirebaseAuth.getInstance();

       profileName = findViewById(R.id.nameTitle);
       profileEmail = findViewById(R.id.emailAddressTitle);
       profileAge = findViewById(R.id.ageTitle);
       updateProfile = (Button) findViewById(R.id.editProfile);

       updateProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               updateInfo();
           }

           private void updateInfo() {


           }
       });

       profileEmail.setText(email);
       profileName.setText(fullName);
       profileAge.setText(age);


     //   Log.d(TAG, "onCreate: " + fullName + " " + email + " " + age);



    }
}