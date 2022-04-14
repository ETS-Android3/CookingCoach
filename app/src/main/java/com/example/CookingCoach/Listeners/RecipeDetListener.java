package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeDetRes;

public interface RecipeDetListener {
    void gotInfo(RecipeDetRes response, String message);
    void gotError(String message);
}
