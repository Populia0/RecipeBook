package com.recipebook.android.ui.favourites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.recipebook.android.db.DBRepository;
import com.recipebook.android.db.entities.Meal;

import java.util.List;
import java.util.concurrent.Executors;

public class FavouritesViewModel extends AndroidViewModel {

    private final DBRepository repository;
    private final LiveData<List<Meal>> favouriteMeals;
    private final MutableLiveData<List<Meal>> meals = new MutableLiveData<>();

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        repository = new DBRepository(application);
        favouriteMeals = repository.getFavoriteMeals();
    }

    public LiveData<List<Meal>> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void toggleIsFavoriteCheckBox(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            repository.setFavoriteMeal(meal);
        });
    }
}