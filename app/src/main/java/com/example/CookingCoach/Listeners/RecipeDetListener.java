package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeDetRes;

public interface RecipeDetListener {
    void didFetch(RecipeDetRes response, String message);
    void didError(String message);
}
