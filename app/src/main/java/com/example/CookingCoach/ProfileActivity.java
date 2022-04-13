package com.example.CookingCoach;

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
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.Bitmap;


import com.example.CookingCoach.Adapters.RandomRecipeAdapter;
import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Listeners.RecipeClick;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;
import com.example.CookingCoach.ml.MobilenetV110224Quant;
import com.google.firebase.auth.FirebaseAuth;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();
    ImageButton camera;
    int imageSize = 32;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        camera = findViewById(R.id.imageButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if((checkSelfPermission(Manifest.permission.CAMERA)) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,3);
                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }

            }
        });


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
    public void classifyImage(Bitmap image){
        try {
            MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipieApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recyler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(ProfileActivity.this, response.recipes,recipeClick);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        }




    };

    private final RecipeClick recipeClick = new RecipeClick() {
        @Override
        public void onRecipeClicked(String id) {
            //Toast.makeText(ProfileActivity.this, id, Toast.LENGTH_SHORT);
            startActivity(new Intent(ProfileActivity.this,RecipieDetActivity.class)
            .putExtra("id",id));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(resultCode == 3)
            {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}