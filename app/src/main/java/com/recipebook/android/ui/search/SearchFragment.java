package com.recipebook.android.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private MealAdapter adapter;

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

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MealAdapter(getContext(), searchViewModel);
        recyclerView.setAdapter(adapter);

        //searchViewModel.getAllMeals().observe(getViewLifecycleOwner(), meals -> {
        //    adapter.setMeals(meals);
        //});

        SearchView searchView = binding.searchView;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.filterMeals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewModel.filterMeals(newText);
                return true;
            }
        });

        searchViewModel.getFilteredMeals().observe(getViewLifecycleOwner(), meals -> {
            adapter.setMeals(meals);
        });

        searchViewModel.getAllMeals().observe(getViewLifecycleOwner(), meals -> {
            if (meals != null) {
                searchViewModel.init();
            }
        });

        return root;
    }

    //@Override
    //public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //    inflater.inflate(R.menu.search_menu, menu);
    //    MenuItem search = menu.findItem(R.id.searchView);
    //    SearchView sv =(SearchView) MenuItemCompat.getActionView(search);
    //    super.onCreateOptionsMenu(menu, inflater);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
