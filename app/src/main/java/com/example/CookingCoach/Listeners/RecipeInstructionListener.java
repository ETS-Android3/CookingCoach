package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeDetRes;
import com.example.CookingCoach.Models.RecipeInstructionsResponse;

public interface RecipeInstructionListener {
    void gotInfo(RecipeInstructionsResponse response, String message);
    void gotError(String message);
}
