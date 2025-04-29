package com.recipebook.android.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.R;
import com.recipebook.android.databinding.FragmentSearchBinding;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    ArrayList<Meal> meals = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // начальная инициализация списка
        setInitialData();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        // создаем адаптер
        MealAdapter adapter = new MealAdapter(requireActivity(), meals);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        return root;
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