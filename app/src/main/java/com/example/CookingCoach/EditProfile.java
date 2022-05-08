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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;



public class EditProfile extends AppCompatActivity {

    private static final String TAG = "EditProfile";
    EditText profileName, profileEmail, profileAge;
    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseUser user;
    TextInputEditText displayNameEditText;
    FirebaseFirestore fStore;



    //DocumentReference documentReference;
   // FirebaseFirestore db;
    private Button updateProfile;
    //String DISPLAY_NAME = null;
    //private String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        //userId = user.getUid();
        Intent data = getIntent();

       String fullName = data.getStringExtra("fullName");
       String email = data.getStringExtra("email");
       String age = data.getStringExtra("age");
       auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();
       fStore = FirebaseFirestore.getInstance();

        /*
        if(user != null){
            Log.d(TAG, "onCreate: "+ user.getDisplayName());
            if(user.getDisplayName() != null){
                displayNameEditText.setText(user.getDisplayName());
            }

        }
        */

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
                   public void onSuccess(Void aVoid) {
                       //DocumentReference docRef = fStore.collection("Users").document(user.getUid());
                       String userId = user.getUid();
                       Map<String,Object> edited = new HashMap<>();
                       //String edited ;
                       edited.put("email", email);
                       edited.put("fullName", profileName.getText().toString());
                       edited.put("age", profileAge.getText().toString());

                       reference.child(userId).updateChildren(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getApplicationContext(),MainActivity.class));
                               finish();
                           }
                       });

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

               UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(DISPLAY_NAME).build();

               user.updateProfile(changeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
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



    } //onCreate


}

/*
Sources
https://www.youtube.com/watch?v=RiHGwJ_u27k
https://www.youtube.com/watch?v=mlH2ct-ZadU

 */