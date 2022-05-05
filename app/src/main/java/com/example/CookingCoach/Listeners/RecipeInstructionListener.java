package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeDetRes;
import com.example.CookingCoach.Models.RecipeInstructionsResponse;

import java.util.List;

public interface RecipeInstructionListener {
    void gotInfo(List<RecipeInstructionsResponse> response, String message);
    void gotError(String message);
}
