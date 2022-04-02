package com.example.CookingCoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.solver.ArrayLinkedVariables;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.CookingCoach.Adapters.RandomRecipeAdapter;
import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query.toString());
                manager.getRandomRecipies(randomRecipeResponseListener, tags);
                dialog.show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

        manager = new RequestManager(this);
       // manager.getRandomRecipies(randomRecipeResponseListener);
        //dialog.show();
    }
    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipieApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recyler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(ProfileActivity.this, response.recipes);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        }




    };
}