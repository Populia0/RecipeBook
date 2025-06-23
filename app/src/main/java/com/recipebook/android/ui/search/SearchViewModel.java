package com.recipebook.android.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executors;

import com.recipebook.android.db.DBRepository;
import com.recipebook.android.db.entities.Meal;

public class SearchViewModel extends AndroidViewModel {

    private final DBRepository repository;
    private final LiveData<List<Meal>> allMeals;
    private final MutableLiveData<List<Meal>> meals = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new DBRepository(application);
        allMeals = repository.getAllMeals(); // получаем LiveData из репозитория
    }

    public LiveData<List<Meal>> getAllMeals() {
        return allMeals;
    }

    public void toggleIsFavoriteCheckBox(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            repository.setFavoriteMeal(meal);
        });
    }
}