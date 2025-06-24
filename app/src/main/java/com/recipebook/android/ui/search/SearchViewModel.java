package com.recipebook.android.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import com.recipebook.android.db.DBRepository;
import com.recipebook.android.db.entities.Meal;

public class SearchViewModel extends AndroidViewModel {

    private final DBRepository repository;
    private final LiveData<List<Meal>> allMeals;
    private final MutableLiveData<List<Meal>> meals = new MutableLiveData<>();

    private final MutableLiveData<List<Meal>> filteredMeals = new MutableLiveData<>();

    public void init() {
        filteredMeals.setValue(allMeals.getValue());
    }

    public LiveData<List<Meal>> getFilteredMeals() {
        return filteredMeals;
    }

    public void filterMeals(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredMeals.postValue(allMeals.getValue());
            return;
        }

        String lowerQuery = query.toLowerCase(Locale.getDefault());

        List<Meal> all = allMeals.getValue();
        if (all == null) return;

        List<Meal> result = new ArrayList<>();
        for (Meal meal : all) {
            if ((meal.getName() != null && meal.getName().toLowerCase().contains(lowerQuery)) ||
                    (meal.getDescription() != null && meal.getDescription().toLowerCase().contains(lowerQuery))) {
                result.add(meal);
            }
        }

        filteredMeals.setValue(result);
    }

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new DBRepository(application);
        allMeals = repository.getAllMeals();
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