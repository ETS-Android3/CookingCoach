package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeNutrientsResponse;

public interface RecipleNutrientsListener {
    void gotInfo(RecipeNutrientsResponse response, String message);
    void gotError(String message);
}
