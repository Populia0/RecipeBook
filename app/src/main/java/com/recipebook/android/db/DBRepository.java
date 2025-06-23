package com.recipebook.android.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.MealTag;
import com.recipebook.android.db.entities.Measurement;
import com.recipebook.android.db.entities.Recipe;
import com.recipebook.android.db.entities.Tag;

import java.util.ArrayList;
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
        executor.execute(this::setInitialData);
    }

    private void setInitialData(){
        Meal meal1 = new Meal();
        meal1.setName("Блюдо 1");
        meal1.setDescription("Готовится быстро Готовится быстро Готовится быстро Готовится быстро");
        meal1.setIsFavorite(0);
        meal1.setImgUri("");
        recipesDao.insertMeal(meal1);

        Meal meal2 = new Meal();
        meal2.setName("Блюдо 2");
        meal2.setDescription("Завтрак");
        meal2.setIsFavorite(0);
        meal2.setImgUri("");
        recipesDao.insertMeal(meal2);

        Meal meal3 = new Meal();
        meal3.setName("Название название название");
        meal3.setDescription("Описание описание описание");
        meal3.setIsFavorite(0);
        meal3.setImgUri("");
        recipesDao.insertMeal(meal3);

        Meal meal4 = new Meal();
        meal4.setName("Блюдо 4");
        meal4.setDescription("Описание");
        meal4.setIsFavorite(0);
        meal4.setImgUri("");
        recipesDao.insertMeal(meal4);

        Meal meal5 = new Meal();
        meal5.setName("Блюдо 5");
        meal5.setDescription("рарфрфовроырвлорфыволрфыолрволфырвлофрыволрфыолвролфырволфрывлорфыолвролфырв");
        meal5.setIsFavorite(0);
        meal5.setImgUri("");
        recipesDao.insertMeal(meal5);
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

    public LiveData<List<Recipe>> getMealRecipe(Meal meal) {
        return recipesDao.getRecipeForMeal(meal.getId());
    }

    public LiveData<List<MealTag>> getMealTags(Meal meal) {
        return recipesDao.getMealTags(meal.getId());
    }

    public void setFavoriteMeal(Meal meal) {
        meal.setIsFavorite(meal.isFavorite() == 0 ? 1 : 0);
        recipesDao.setFavoriteMeal(meal);
    }

    public void setMealImage(Meal meal, String uri) {
        meal.setImgUri(uri);
        recipesDao.setMealImage(meal);
    }

    public LiveData<List<Meal>> getFavoriteMeals() {
        return recipesDao.getFavoriteMeals();
    }

    public void UpdateMeal(Meal meal, List<Recipe> recipes, List<MealTag> mealTags) {
        recipesDao.updateMealInfo(meal);
        for (Recipe recipe: recipes) {
            recipesDao.updateRecipeInfo(recipe);
        }
        for (MealTag mealTag: mealTags) {
            recipesDao.updateMealTag(mealTag);
        }
    }

    public void deleteRecipe(Recipe recipe) {
        recipesDao.deleteRecipe(recipe.getId());
    }

    public void deleteMeal(Meal meal) {
        recipesDao.deleteMeal(meal.getId());
    }

    public void deleteIngredient(Ingredient ingredient) {
        recipesDao.deleteIngredient(ingredient.getId());
    }

    public void deleteIngredientFromMeal(Ingredient ingredient, Meal meal) {
        recipesDao.deleteIngredientFromMeal(meal.getId(), ingredient.getId());
    }

    public void deleteTag(Tag tag) {
        recipesDao.deleteTag(tag.getId());
    }

    public void deleteTagFromMeal(Tag tag, Meal meal) {
        recipesDao.deleteTagFromMeal(meal.getId(), tag.getId());
    }

    public void deleteMeasurement(Measurement measurement) {
        recipesDao.deleteMeasurement(measurement.getId());
    }

    public LiveData<List<Meal>> getMealsByTags(List<Tag> tags) {
        List<Integer> tagIds = new ArrayList<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }
        return recipesDao.getMealsByTags(tagIds, tagIds.size());
    }

    public LiveData<List<Meal>> getMealsByIngredients(List<Ingredient> ingredients) {
        List<Integer> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientIds.add(ingredient.getId());
        }
        return recipesDao.getMealsByIngredients(ingredientIds, ingredientIds.size());
    }

    public LiveData<List<Meal>> getMealsByIngredientsAndTags(List<Tag> tags, List<Ingredient> ingredients) {
        List<Integer> tagIds = new ArrayList<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }

        List<Integer> ingredientIds = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientIds.add(ingredient.getId());
        }

        return recipesDao.getMealsByIngredientsAndTags(ingredientIds, tagIds, ingredientIds.size(), tagIds.size());
    }

    public void insertMeal(Meal meal) {
        recipesDao.insertMeal(meal);
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
                    long ingredientId = recipesDao.insertIngredient(ingredient);
                    recipe.setIngredientId((int) ingredientId);
                } else {
                    recipe.setIngredientId(existing_i.getId());
                }

                recipe.setAmount(amounts.get(i));

                Measurement measurement = measurements.get(i);
                Measurement existing_m = recipesDao.getMeasurementByName(measurement.getName());
                if (existing_m == null) {
                    long measurementId = recipesDao.insertMeasurement(measurement);
                    recipe.setMeasurementId((int) measurementId);
                } else {
                    recipe.setMeasurementId(existing_m.getId());
                }

                recipesDao.insertRecipe(recipe);
            }

            for (Tag tag: tags) {
                MealTag mealTag = new MealTag();
                mealTag.setMealId((int) mealId);

                Tag existing = recipesDao.getTagByName(tag.getName());
                if (existing == null) {
                    long tagId = recipesDao.insertTag(tag);
                    mealTag.setTagId((int) tagId);
                } else {
                    mealTag.setTagId(existing.getId());
                }

                recipesDao.insertMealTag(mealTag);
            }
        });
    }
}
