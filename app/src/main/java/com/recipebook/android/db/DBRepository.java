package com.recipebook.android.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.MealTag;
import com.recipebook.android.db.entities.Measurement;
import com.recipebook.android.db.entities.Recipe;
import com.recipebook.android.db.entities.Tag;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBRepository {
    private final RecipesDao recipesDao;
    private final Executor executor;

    public DBRepository(Application application) {
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        recipesDao = db.recipesDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Tag>> getAllTags() {
        return recipesDao.getAllTags();
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return recipesDao.getAllIngredients();
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return recipesDao.getAllMeasurements();
    }

    public LiveData<List<Meal>> getAllMeals() {
        return recipesDao.getAllMeals();
    }

    public void insertMeal(Meal meal, List<Ingredient> ingredients, List<Double> amounts, List<Measurement> measurements, List<Tag> tags) {
        executor.execute(() -> {
            long mealId = recipesDao.insertMeal(meal);

            for (int i = 0; i < ingredients.size(); i++) {
                Recipe recipe = new Recipe();
                recipe.setMealId((int) mealId);

                Ingredient ingredient = ingredients.get(i);
                Ingredient existing_i = recipesDao.getIngredientByName(ingredient.getName());
                if (existing_i == null) {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredient.getName());
                    long ingredientId = recipesDao.insertIngredient(newIngredient);
                    recipe.setIngredientId((int) ingredientId);
                } else {
                    recipe.setIngredientId(existing_i.getId());
                }

                recipe.setAmount(amounts.get(i));

                Measurement measurement = measurements.get(i);
                Measurement existing_m = recipesDao.getMeasurementByName(measurement.getName());
                if (existing_m == null) {
                    Measurement newMeasurement = new Measurement();
                    newMeasurement.setName(measurement.getName());
                    long measurementId = recipesDao.insertMeasurement(newMeasurement);
                    recipe.setMeasurementId((int) measurementId);
                } else {
                    recipe.setMeasurementId(existing_m.getId());
                }

                recipesDao.insertRecipe(recipe);
            }

            for (Tag tag: tags) {
                MealTag mealTag = new MealTag();
                mealTag.setMealId((int) mealId);
                mealTag.setTagId(tag.getId());
            }
        });
    }
}
