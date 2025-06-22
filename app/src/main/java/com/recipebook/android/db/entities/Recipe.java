package com.recipebook.android.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recipes",
        foreignKeys = {
            @ForeignKey(
                    entity = Meal.class,
                    parentColumns = "id",
                    childColumns = "meal_id",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Ingredient.class,
                    parentColumns = "id",
                    childColumns = "ingredient_id",
                    onDelete = ForeignKey.CASCADE
            )
        })
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "meal_id")
    private int mealId;

    @ColumnInfo(name = "ingredient_id")
    private int ingredientId;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "measurement_id")
    private int measurementId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public int getMeasurementId() { return measurementId; }
    public void setMeasurementId(int measurementId) { this.measurementId = measurementId; }
}
