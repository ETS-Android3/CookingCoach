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
import com.example.CookingCoach.Listeners.RecipeSummaryListener;
import com.example.CookingCoach.Listeners.RecipeTasteListener;
import com.example.CookingCoach.Listeners.RecipleNutrientsListener;
import com.example.CookingCoach.Models.RecipeDetRes;
import com.example.CookingCoach.Models.RecipeNutrientsResponse;
import com.example.CookingCoach.Models.RecipeSummaryResponse;
import com.example.CookingCoach.Models.RecipeTasteResponse;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.StringJoiner;

public class RecipieDetActivity extends AppCompatActivity {
    int id;
    TextView mealName;
    TextView mealCalories, mealCarbs, mealFat, mealProtien, mealSweetness, mealSaltiness, mealSourness, mealBitterness, mealSavoriness, mealSpiciness, mealSummary, mealLink;
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

        requestmana.getRecipeTaste(recipeTasteListener,id);

        requestmana.getRecipeSummary(recipeSummaryListener,id);

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

        mealSweetness = findViewById(R.id.mealSweetness);
        mealSaltiness = findViewById(R.id.mealSaltiness);
        mealBitterness = findViewById(R.id.mealBitterness);
        mealSourness = findViewById(R.id.mealSourness);
        mealSavoriness = findViewById(R.id.mealSavoriness);
        //mealFattiness = findViewById(R.id.mealFattiness);
        mealSpiciness = findViewById(R.id.mealSpiciness);
        mealProtien = findViewById(R.id.mealProtien);

        mealSummary = findViewById(R.id.mealSummary);
        mealLink = findViewById(R.id.mealLink);

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
    private  final RecipeTasteListener recipeTasteListener = new RecipeTasteListener() {
        @Override
        public void gotInfo(RecipeTasteResponse response, String message) {
            //mealSweetness.setText(String.valueOf(response.sweetness));
            mealSweetness.setText("Sweetness: "+Double.toString(response.sweetness));
            //mealSaltiness.setText(String.valueOf(response.saltiness));
            mealSaltiness.setText("Saltiness: "+Double.toString(response.saltiness));
            //mealSourness.setText(String.valueOf(response.sourness));
            mealSourness.setText("Sourness: "+Double.toString(response.sourness));
            //mealBitterness.setText(String.valueOf(response.bitterness));
            mealBitterness.setText("Bitterness: "+Double.toString(response.bitterness));
            //mealSavoriness.setText(String.valueOf(response.savoriness));
            mealSavoriness.setText("Savoriness: "+Double.toString(response.savoriness));
            //mealFattiness.setText(String.valueOf(response.fattiness));
            //mealFattiness.setText(Integer.toString(response.fattiness));
            //mealSpiciness.setText(String.valueOf(response.spiciness));
            mealSpiciness.setText("Spiciness: "+Double.toString(response.spiciness));
        }

        @Override
        public void gotError(String message) {
            Toast.makeText(RecipieDetActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };
    private final RecipeSummaryListener recipeSummaryListener = new RecipeSummaryListener() {
        @Override
        public void gotInfo(RecipeSummaryResponse response, String message) {
            progressdia.dismiss();

            String sub[] = response.summary.split("<b>");
            //StringJoiner sb = new StringJoiner(" ");
            String s = "";
            for(int i=0;i<sub.length;i++)
            {
                s +=sub[i];
                //s.join(" ",sub[i]);
                //sb.add(sub[i]);
            }
            //String s = Arrays.toString(sub);
            String sub2[] = s.split("</b>");
            //StringJoiner sb2 = new StringJoiner(" ");
            String s2 = "";
            for(int i=0;i<sub2.length;i++)
            {
                s2+=sub2[i];
                //sb2.add(sub2[i]);
            }


            String sub3[] = s2.split("<a");
            String s3 = sub3[0];
            String link = "";
            for(int i =0;i< sub3.length;i++)
            {
                link+=sub3[i];
            }
            mealSummary.setText(s3);

            String linksub[] = link.split( "\"");
            String links = linksub[1]+" "+ linksub[3];
            mealLink.setText(links);

        }

        @Override
        public void gotError(String message) {
            Toast.makeText(RecipieDetActivity.this,message,Toast.LENGTH_SHORT).show();

        }
    };
}