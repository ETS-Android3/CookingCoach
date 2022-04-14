package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RandomRecipieApiResponse;

public interface RandomRecipeResponseListener {
    void gotInfo(RandomRecipieApiResponse response, String message);
    void gotError(String message);
}
