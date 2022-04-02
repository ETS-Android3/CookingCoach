package com.example.CookingCoach;

import android.content.Context;

import com.example.CookingCoach.Listeners.RandomRecipeResponseListener;
import com.example.CookingCoach.Models.RandomRecipieApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
        Call<RandomRecipieApiResponse> call = callRandomRecipies.callRandom(context.getString(R.string.api_key),"10", tags);
        call.enqueue(new Callback<RandomRecipieApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipieApiResponse> call, Response<RandomRecipieApiResponse> response) {
                if(!response.isSuccessful())
                {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipieApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallRandomRecipies
    {@GET("recipes/random")
        Call<RandomRecipieApiResponse> callRandom(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }
}
