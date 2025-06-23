package com.recipebook.android.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.MealTag;
import com.recipebook.android.db.entities.Measurement;
import com.recipebook.android.db.entities.Recipe;
import com.recipebook.android.db.entities.Tag;

@Database(
        entities = {
            Ingredient.class,
            Meal.class,
            MealTag.class,
            Measurement.class,
            Recipe.class,
            Tag.class},
        version = 1,
        exportSchema = false
)
public abstract class DatabaseHelper extends RoomDatabase {
    public static final String DB_NAME = "RecipesDB.db";
    public abstract RecipesDao recipesDao();
    public static volatile DatabaseHelper INSTANCE;

    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DatabaseHelper.class,
                            DB_NAME
                    )
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    })
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
