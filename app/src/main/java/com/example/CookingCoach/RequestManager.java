package com.example.CookingCoach;

import android.content.Context;

import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Listeners.RecipeDetListener;
import com.example.CookingCoach.Listeners.RecipeInstructionListener;
import com.example.CookingCoach.Listeners.RecipeSummaryListener;
import com.example.CookingCoach.Listeners.RecipeTasteListener;
import com.example.CookingCoach.Listeners.RecipleNutrientsListener;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;
import com.example.CookingCoach.Models.RecipeDetRes;
import com.example.CookingCoach.Models.RecipeInstructionsResponse;
import com.example.CookingCoach.Models.RecipeNutrientsResponse;
import com.example.CookingCoach.Models.RecipeSummaryResponse;
import com.example.CookingCoach.Models.RecipeTasteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipies(RandomRecipeResponseListener listener, List<String> tags){
        CallRandomRecipies callRandomRecipies = retrofit.create(CallRandomRecipies.class);
        Call<RandomRecipieApiResponse> call = callRandomRecipies.callRandom(context.getString(R.string.api_key),"15", tags);
        call.enqueue(new Callback<RandomRecipieApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipieApiResponse> call, Response<RandomRecipieApiResponse> response) {
                if(!response.isSuccessful())
                {
                    listener.gotError(response.message());
                    return;
                }
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipieApiResponse> call, Throwable t) {
                listener.gotError(t.getMessage());
            }
        });
    }
    //create a function to get the info
    public void getRecipeDet(RecipeDetListener listener, int id)
    {
        CallRecipe callRecipe = retrofit.create(CallRecipe.class);
        Call<RecipeDetRes> call = callRecipe.callRecipeDet(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetRes>() {
            @Override
            public void onResponse(Call<RecipeDetRes> call, Response<RecipeDetRes> response) {
                if(!response.isSuccessful())
                {
                    listener.gotError(response.message());
                    return;
                }
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetRes> call, Throwable t) {
                listener.gotError(t.getMessage());
            }
        });
    }
    //create a function to get the info
    public void getRecipeNutrients(RecipleNutrientsListener listener, int id)
    {
        //create a retro fit for the call
        CallNutrients callNutrients = retrofit.create(CallNutrients.class);
        //pass in the call parameters
        Call<RecipeNutrientsResponse> call = callNutrients.callRecipeNutrients(id, context.getString(R.string.api_key));
        //enqueue the call and see what it gives back
        call.enqueue(new Callback<RecipeNutrientsResponse>() {
            @Override
            public void onResponse(Call<RecipeNutrientsResponse> call, Response<RecipeNutrientsResponse> response) {
                //if the response is not successful
                if(!response.isSuccessful())
                {
                    //then we say we got a error
                    listener.gotError(response.message());
                }
                //otherwise we return the body
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeNutrientsResponse> call, Throwable t) {
                //then on failure we just give back the error
                listener.gotError((t.getMessage()));
            }
        });
    }
    public void getRecipeSummary(RecipeSummaryListener listener, int id)
    {
        CallRecipeSummary callRecipeSummary = retrofit.create(CallRecipeSummary.class);
        Call<RecipeSummaryResponse> call = callRecipeSummary.callRecipeSummary(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeSummaryResponse>() {
            @Override
            public void onResponse(Call<RecipeSummaryResponse> call, Response<RecipeSummaryResponse> response) {
                if(!response.isSuccessful())
                {
                    //then we say we got a error
                    listener.gotError(response.message());
                }
                //otherwise we return the body
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeSummaryResponse> call, Throwable t) {
                //then on failure we just give back the error
                listener.gotError((t.getMessage()));
            }
        });
    }
    public void getRecipeTaste(RecipeTasteListener listener, int id)
    {
        CallRecipeTaste callRecipeTaste = retrofit.create(CallRecipeTaste.class);
        Call<RecipeTasteResponse> call = callRecipeTaste.callRecipeTase(id,context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeTasteResponse>() {
            @Override
            public void onResponse(Call<RecipeTasteResponse> call, Response<RecipeTasteResponse> response) {
                if(!response.isSuccessful())
                {
                    //then we say we got a error
                    listener.gotError(response.message());
                }
                //otherwise we return the body
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeTasteResponse> call, Throwable t) {
                //then on failure we just give back the error
                listener.gotError((t.getMessage()));
            }
        });
    }


    public void getRecipeInstructions(RecipeInstructionListener listener, int id)
    {
        CallRecipeInstructions callRecipeInstructions = retrofit.create(CallRecipeInstructions.class);
        Call<List<RecipeInstructionsResponse>> call = callRecipeInstructions.callRecipeInstructions(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<List<RecipeInstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeInstructionsResponse>> call, Response<List<RecipeInstructionsResponse>> response) {
                if(!response.isSuccessful())
                {
                    //then we say we got a error
                    listener.gotError(response.message());
                }
                //otherwise we return the body
                listener.gotInfo(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<RecipeInstructionsResponse>> call, Throwable t) {
                //then on failure we just give back the error
                listener.gotError((t.getMessage()));
            }
        });
    }
    private interface CallRandomRecipies
    {
        //set the link to get the info we need from the api
        @GET("recipes/random")
        Call<RandomRecipieApiResponse> callRandom(
                //pass in the needed arguments for the link
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }
    private interface CallRecipe
    {
        //set the link to get the info we need from the api
        @GET("recipes/{id}/information")
        Call<RecipeDetRes> callRecipeDet(
                //pass in the needed arguments for the link
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallNutrients
    {
        //set the link to get the info we need from the api
        @GET("recipes/{id}/nutritionWidget.json")
        Call<RecipeNutrientsResponse> callRecipeNutrients(
                //pass in the needed arguments for the link
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallRecipeSummary
    {
        @GET("recipes/{id}/summary")
        Call<RecipeSummaryResponse> callRecipeSummary(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallRecipeTaste
    {
        @GET("recipes/{id}/tasteWidget.json")
        Call<RecipeTasteResponse> callRecipeTase(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
    private interface CallRecipeInstructions
    {
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<RecipeInstructionsResponse>> callRecipeInstructions(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}
