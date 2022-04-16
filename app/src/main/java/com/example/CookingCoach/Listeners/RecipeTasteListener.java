package com.example.CookingCoach.Listeners;


import com.example.CookingCoach.Models.RecipeTasteResponse;

public interface RecipeTasteListener {
    void gotInfo(RecipeTasteResponse response, String message);
    void gotError(String message);
}
