package com.example.CookingCoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CookingCoach.Adapters.IngrAdapt;
import com.example.CookingCoach.Listeners.RecipeDetListener;
import com.example.CookingCoach.Listeners.RecipleNutrientsListener;
import com.example.CookingCoach.Models.RecipeDetRes;
import com.example.CookingCoach.Models.RecipeNutrientsResponse;
import com.squareup.picasso.Picasso;

public class RecipieDetActivity extends AppCompatActivity {
    int id;
    TextView mealName;
    TextView mealCalories, mealCarbs, mealFat, mealProtien;
    ImageView mealImage;
    RecyclerView mealIngredients;
    RequestManager requestmana;
    ProgressDialog progressdia;
    IngrAdapt adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipie_det);

        //create the the way to call the variables in the layout xml
        findIds();

        //get the id from the intent
        id = Integer.parseInt(getIntent().getStringExtra("id"));

        //initialize the request manager
        requestmana = new RequestManager(this);

        //call the api(s) to get details
        requestmana.getRecipeDet(recipeDetlisten,id);

        requestmana.getRecipeNutrients(RecipleNutrientsListener,id);

        //set up dialog
        //just a waiting screen (can be removed, not really needed)
        progressdia = new ProgressDialog(this);
        progressdia.setTitle("Loading Details...");
        progressdia.show();
    }

    private void findIds() {
        //initialize the ids
        mealName = findViewById(R.id.mealName);
        //mealSource = findViewById(R.id.mealSource);
        mealImage = findViewById(R.id.mealImage);
        mealIngredients = findViewById(R.id.mealIngredients);

        mealCalories = findViewById(R.id.mealCalories);
        mealCarbs = findViewById(R.id.mealCarbs);
        mealFat = findViewById(R.id.mealFat);
        mealProtien = findViewById(R.id.mealProtien);

    }

    //create the recipe detail listener object
    private final RecipeDetListener recipeDetlisten = new RecipeDetListener() {
        @Override
        public void gotInfo(RecipeDetRes response, String message) {
            progressdia.dismiss();

            //set the meal name from the response
            mealName.setText(response.title);

            //set the meal image
            Picasso.get().load(response.image).into(mealImage);

            //set the ingredient
            mealIngredients.setHasFixedSize(true);
            mealIngredients.setLayoutManager(new LinearLayoutManager(RecipieDetActivity.this,LinearLayoutManager.VERTICAL,false));

            //set the adapter
            adapt = new IngrAdapt(RecipieDetActivity.this,response.extendedIngredients);
            mealIngredients.setAdapter(adapt);
        }

        @Override
        public void gotError(String message) {
            //show a message with a error
            Toast.makeText(RecipieDetActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };

    //create the nutrient detail listener object
    private final RecipleNutrientsListener RecipleNutrientsListener = new RecipleNutrientsListener()
    {

        @Override
        public void gotInfo(RecipeNutrientsResponse response, String message) {
            progressdia.dismiss();

            //set the calories from the response
            mealCalories.setText("Calories: "+response.calories);

            //set the meal carbs from the response
            mealCarbs.setText("Carbs: "+response.carbs);

            //set the meal fat from the response
            mealFat.setText("Fat: "+response.fat);

            //set the meal protein
            mealProtien.setText("Protein: "+response.protein);
        }

        @Override
        public void gotError(String message) {

            //show a message with a error
            Toast.makeText(RecipieDetActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };
}