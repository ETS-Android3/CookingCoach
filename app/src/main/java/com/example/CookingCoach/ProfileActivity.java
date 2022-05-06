package com.example.CookingCoach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.solver.ArrayLinkedVariables;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import java.io.InputStream;


import com.example.CookingCoach.Adapters.RandomRecipeAdapter;
import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Listeners.RecipeClick;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;
import com.example.CookingCoach.ml.MobilenetV110224Quant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog dialog;
    Bitmap bitmap;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;


    private Button logout;
    private Button editProfile;
    List<String> tags = new ArrayList<>();


      @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_profile);
          dialog = new ProgressDialog(this);
          dialog.setTitle("Loading...");

          //initialize the search view
          searchView = findViewById(R.id.searchView);
          logout = (Button) findViewById(R.id.signOut);
          editProfile = (Button) findViewById(R.id.editProfile);

          logout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  FirebaseAuth.getInstance().signOut();
                  startActivity(new Intent(ProfileActivity.this,MainActivity.class));
              }

          });

          editProfile.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(ProfileActivity.this, EditProfile.class));


              }
          });


          //create a text listener for the search view
          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  //clear all of the tags first
                  tags.clear();
                  //then add the search to the tags
                  tags.add(query.toString());
                  //then search the tag made
                  manager.getRandomRecipies(randomRecipeResponseListener, tags);
                  //then show th results
                  dialog.show();

                  return true;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  return false;
              }


          });


          user =  FirebaseAuth.getInstance().getCurrentUser();
          reference = FirebaseDatabase.getInstance().getReference("Users");
          userId = user.getUid();

          final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
          final TextView _greetingTextView = (TextView) findViewById(R.id.greeting1);
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

                      greetingTextView.setText("   Enter any ingredients, " + name + " !!");
                      _greetingTextView.setText("  Welcome, " + name + " ! ");
                      nameTextView.setText(name);
                      emailTextView.setText(email);
                      ageTextView.setText(age);
                  }


              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                  Toast.makeText(ProfileActivity.this, "Error getting the data!", Toast.LENGTH_LONG).show();

              }
          });

          manager = new RequestManager(this);
          // manager.getRandomRecipies(randomRecipeResponseListener);
          //dialog.show();
       }



    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void gotInfo(RandomRecipieApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recyler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(ProfileActivity.this, response.recipes,recipeClick);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void gotError(String message) {
            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        }




    };

    private final RecipeClick recipeClick = new RecipeClick() {
        @Override
        public void onRecipeClicked(String id) {
            //Toast.makeText(ProfileActivity.this, id, Toast.LENGTH_SHORT);
            //if the recipe is clicked then we start a new activity which gets us more info of the recipe
            startActivity(new Intent(ProfileActivity.this,RecipieDetActivity.class)
            .putExtra("id",id));
        }
    };


}