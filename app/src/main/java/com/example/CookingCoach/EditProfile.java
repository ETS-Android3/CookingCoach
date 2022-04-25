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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText profileName, profileEmail, profileAge;
    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseUser user;

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
       user = auth.getCurrentUser();

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
               if(profileName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profileAge.getText().toString().isEmpty())
               {
                   Toast.makeText(EditProfile.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                   return;
               }

               String email = profileEmail.getText().toString();
               user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       //DatabaseReference dataRef = database.collection()
                       //Map<String,Object> edited = new HashMap<>();
                       //edited.put("email", email);

                       Toast.makeText(EditProfile.this, "Email updated", Toast.LENGTH_SHORT).show();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

               //String name = profileName.getText().toString();
               /*

               user.updateProfile().addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(EditProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                   }
               });

                */





           }
       });

       profileEmail.setText(email);
       profileName.setText(fullName);
       profileAge.setText(age);


     //   Log.d(TAG, "onCreate: " + fullName + " " + email + " " + age);



    }
}