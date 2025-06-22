package com.recipebook.android.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.MealTag;
import com.recipebook.android.db.entities.Measurement;
import com.recipebook.android.db.entities.Recipe;
import com.recipebook.android.db.entities.Tag;

import java.util.List;

@Dao
public interface RecipesDao {
    // Теги
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTag(Tag tag);
    @Query("SELECT * FROM Tags")
    List<Tag> getAllTags();
    @Query("DELETE FROM Tags WHERE id = :tagId")
    void deleteTag(int tagId);

    // Ингредиенты
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTag(Ingredient ingredient);
    @Query("SELECT * FROM Ingredients")
    List<Ingredient> getAllIngredients();
    @Query("DELETE FROM Ingredients WHERE id = :ingredientId")
    void deleteIngredient(int ingredientId);

    // Измерения
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeasurement(Measurement measurement);
    @Query("SELECT * FROM Measurements")
    List<Measurement> getAllMeasurements();
    @Query("DELETE FROM Measurements WHERE id = :measurementId")
    void deleteMeasurement(int measurementId);

    // Блюда
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(Meal meal);
    @Query("SELECT * FROM Meals")
    List<Meal> getAllMeals();
    @Query("SELECT * FROM Meals WHERE is_favorite = :favorite")
    List<Meal> getFavoriteMeals(int favorite);
    @Query("DELETE FROM Meals WHERE id = :mealId")
    void deleteMeal(int mealId);

    // Рецепты
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);
    @Query("SELECT * FROM Recipes WHERE meal_id = :mealId")
    List<Recipe> getRecipeForMeal(int mealId);
    @Query("DELETE FROM Recipes WHERE meal_id = :mealId")
    void deleteRecipe(int mealId);
    @Query("DELETE FROM Recipes WHERE meal_id = :mealId AND ingredient_id = :ingredientId")
    void deleteIngredientFromMeal(int mealId, int ingredientId);
    @Query("SELECT * FROM meals WHERE id IN (" +
            "SELECT meal_id FROM recipes " +
            "WHERE ingredient_id IN (:ingredientIds) " +
            "GROUP BY meal_id " +
            "HAVING COUNT(DISTINCT ingredient_id) = :ingredientCount)")
    List<Meal> getMealsByIngredients(List<Integer> ingredientIds, int ingredientCount);
    @Query("SELECT * FROM meals WHERE id IN (" +
            "SELECT meal_id FROM MealTags " +
            "WHERE tag_id IN (:tagIds) " +
            "GROUP BY meal_id " +
            "HAVING COUNT(DISTINCT tag_id) = :tagCount)")
    List<Meal> getMealsByTags(List<Integer> tagIds, int tagCount);
    @Query("SELECT * FROM meals WHERE id IN (" +
            "SELECT meal_id FROM recipes " +
            "WHERE ingredient_id IN (:ingredientIds) " +
            "GROUP BY meal_id " +
            "HAVING COUNT(DISTINCT ingredient_id) = :ingredientCount) " +
            "AND id IN (" +
            "SELECT meal_id FROM MealTags " +
            "WHERE tag_id IN (:tagIds) " +
            "GROUP BY meal_id " +
            "HAVING COUNT(DISTINCT tag_id) = :tagCount)")
    List<Meal> getMealsByIngredientsAndTags(List<Integer> ingredientIds, List<Integer> tagIds, int ingredientCount, int tagCount);

    // Блюда по тегам
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealTag(MealTag mealTag);
    @Query("SELECT * FROM MealTags WHERE meal_id = :mealId")
    List<MealTag> getMealTags(int mealId);
    @Query("DELETE FROM MealTags WHERE meal_id = :mealId AND tag_id = :tagId")
    void deleteTagFromMeal(int mealId, int tagId);
}
