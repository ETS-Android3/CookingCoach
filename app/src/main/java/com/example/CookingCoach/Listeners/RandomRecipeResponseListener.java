package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RandomRecipieApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipieApiResponse response, String message);
    void didError(String message);
}
