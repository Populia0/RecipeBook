package com.recipebook.android.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.R;
import com.recipebook.android.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private final ArrayList<Meal> meals = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // начальная инициализация списка
        setInitialData();
        RecyclerView recyclerView = binding.recyclerView;
        // создаем адаптер
        MealAdapter adapter = new MealAdapter(getContext(), meals);
        Log.d("DEBUG", "binding.recyclerView = " + binding.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.searchView);
        SearchView sv =(SearchView) MenuItemCompat.getActionView(search);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setInitialData(){
        meals.add(new Meal ("Блюдо 1", "Готовится быстро Готовится быстро Готовится быстро Готовится быстро", R.drawable.test));
        meals.add(new Meal ("Блюдо 2", "Завтрак", R.drawable.test));
        meals.add(new Meal ("Название название название", "Описание описание описание", R.drawable.test));
        meals.add(new Meal ("Блюдо 4", "Описание", R.drawable.test));
        meals.add(new Meal ("Блюдо 5", "рарфрфовроырвлорфыволрфыолрволфырвлофрыволрфыолвролфырволфрывлорфыолвролфырв", R.drawable.test));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}