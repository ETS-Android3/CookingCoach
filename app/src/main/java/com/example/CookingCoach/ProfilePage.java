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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    private Button logout;
    private Button homepage;
    private Button editProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        logout = (Button) findViewById(R.id.signOut);
        homepage = (Button) findViewById(R.id.home);
        editProfile = (Button) findViewById(R.id.editProfile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfilePage.this,MainActivity.class));
            }

        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(ProfilePage.this, ProfileActivity.class));

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, EditProfile.class));


            }
        });

        user =  FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView nameTextView = (TextView) findViewById(R.id.name);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);
        final TextView ageTextView = (TextView) findViewById(R.id.age);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null)
                {
                    String name = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    greetingTextView.setText("Welcome, " + name + ".");
                    nameTextView.setText(name);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePage.this, "Error getting the data!", Toast.LENGTH_LONG).show();

            }
        });




    }
}