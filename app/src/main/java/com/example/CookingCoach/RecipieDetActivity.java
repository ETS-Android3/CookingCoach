package com.example.CookingCoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    PieChart pieChart;
    BarChart barChart;

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

        pieChart = findViewById(R.id.pieChart);

        barChart = findViewById(R.id.barChart);

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

            String total = response.carbs+response.fat+response.protein;
            String sub[] = total.split("g");
            float num1 = Float.parseFloat(sub[0]);
            float num2 = Float.parseFloat(sub[1]);
            float num3 = Float.parseFloat(sub[2]);

            //set basic format for pie chart
            pieChart.setDrawHoleEnabled(true);
            pieChart.setUsePercentValues(true);
            pieChart.setEntryLabelTextSize(12);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setCenterText("MacroNutrients");
            pieChart.setCenterTextSize(20);
            pieChart.getDescription().setEnabled(false);

            //legend for piechart
            Legend legend = pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setEnabled(true);





            //add data to pie chart
            ArrayList<PieEntry> entries = new ArrayList<>();
            //entries.add(new PieEntry(0.3f,"Calories"));
            entries.add(new PieEntry(num1,"Carbs"));
            entries.add(new PieEntry(num2,"Fat"));
            entries.add(new PieEntry(num3,"Protein"));

            //get color
            ArrayList<Integer> colors = new ArrayList<>();
            for (int color: ColorTemplate.MATERIAL_COLORS)
            {
                colors.add(color);
            }
            for (int color : ColorTemplate.VORDIPLOM_COLORS)
            {
                colors.add(color);
            }
            //create dataset
            PieDataSet dataSet = new PieDataSet(entries,"Macronutrients");
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(pieChart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);


            //send data to pie chart
            pieChart.setData(data);
            pieChart.invalidate();

            //animation for pie chart
            pieChart.animateX(1400, Easing.EaseInOutQuad);
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
            if(response.spiciness>100)
            {
                response.spiciness=100;
            }
            mealSpiciness.setText("Spiciness: "+Double.toString(response.spiciness));


            ArrayList barArrayList = new ArrayList();
            barArrayList.add(new BarEntry(1, (float) response.sweetness));
            barArrayList.add(new BarEntry(2, (float) response.saltiness));
            barArrayList.add(new BarEntry(3, (float) response.sourness));
            barArrayList.add(new BarEntry(4, (float) response.bitterness));
            barArrayList.add(new BarEntry(5, (float) response.savoriness));
            barArrayList.add(new BarEntry(6, (float) response.spiciness));



            BarDataSet barDataSet = new BarDataSet(barArrayList,
                    "Sweet"+"               Salt"+"                 Sour"+"                 Bitter"+"              Savor"+"             Spicy");
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(12);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(2000);

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


            String sub3[] = s2.split("%");
            String s3 = "";
            for(int i =0;i<sub3.length-1;i++)
            {
                s3+=sub3[i];
            }
            String link = "";
            for(int i =0;i<sub3.length;i++)
            {
                link+=sub3[i];
            }
            mealSummary.setText(s3+" out of 100");

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