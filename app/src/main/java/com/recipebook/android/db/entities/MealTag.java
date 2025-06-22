package com.recipebook.android.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "MealTags",
        foreignKeys = {
            @ForeignKey(
                    entity = Meal.class,
                    parentColumns = "id",
                    childColumns = "meal_id",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Tag.class,
                    parentColumns = "id",
                    childColumns = "tag_id",
                    onDelete = ForeignKey.CASCADE
            )
        })
public class MealTag {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "meal_id")
    private int mealId;
    @ColumnInfo(name = "tag_id")
    private int tagId;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMealId() { return mealId; }
    public void setMealId(int id) { this.mealId = id; }
    public int getTagId() { return tagId; }
    public void setTagId(int id) { this.tagId = id; }
}
