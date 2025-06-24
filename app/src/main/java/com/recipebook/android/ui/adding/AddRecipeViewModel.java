package com.recipebook.android.ui.adding;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.recipebook.android.db.DBRepository;

import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.Measurement;
import com.recipebook.android.db.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeViewModel extends AndroidViewModel {
    private final DBRepository repository;

    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new DBRepository(application);
    }

    public void insertMeal(Meal meal, List<Ingredient> selectedIngredients) {
        List<Double> amounts = new ArrayList<Double>();
        List<Measurement> measurements = new ArrayList<Measurement>();
        List<Tag> tags = new ArrayList<Tag>();
        repository.insertMeal(meal, selectedIngredients, amounts, measurements, tags);
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return repository.getAllIngredients();
    }

    public void insertIngredient(Ingredient newIngredient) {
        repository.insertIngredient(newIngredient);
    }

    public void insertTag(Tag newTag) {
        repository.insertTag(newTag);
    }

    public LiveData<List<Tag>> getAllTags() {
        return repository.getAllTags();
    }
}

