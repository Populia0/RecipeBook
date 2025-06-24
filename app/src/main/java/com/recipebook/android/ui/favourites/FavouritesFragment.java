package com.recipebook.android.ui.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.R;
import com.recipebook.android.databinding.FragmentFavouritesBinding;
import com.recipebook.android.databinding.FragmentSearchBinding;
import com.recipebook.android.ui.favourites.FavouriteMealAdapter;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesViewModel viewModel;
    private FavouriteMealAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        adapter = new FavouriteMealAdapter(getContext(), viewModel);

        setupToolbar();

        binding.favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.favouritesRecyclerView.setAdapter(adapter);

        viewModel.getFavouriteMeals().observe(getViewLifecycleOwner(), meals -> {
            adapter.setMeals(meals);
        });

        return binding.getRoot();
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.favourites_fragment_title);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requireActivity().setTitle(getString(R.string.favourites_fragment_title));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}