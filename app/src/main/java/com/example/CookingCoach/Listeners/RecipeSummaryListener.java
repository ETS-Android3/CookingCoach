package com.example.CookingCoach.Listeners;

import com.example.CookingCoach.Models.RecipeSummaryResponse;

public interface RecipeSummaryListener {
    void gotInfo(RecipeSummaryResponse response, String message);
    void gotError(String message);
}
