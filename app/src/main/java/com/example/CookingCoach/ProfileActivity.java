package com.example.CookingCoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.CookingCoach.Adapters.RandomRecipeAdapter;
import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        manager = new RequestManager(this);
        manager.getRandomRecipies(randomRecipeResponseListener);
        dialog.show();
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