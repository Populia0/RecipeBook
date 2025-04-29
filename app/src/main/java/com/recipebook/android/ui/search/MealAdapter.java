package com.recipebook.android.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.R;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Meal> meals;

    MealAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealAdapter.ViewHolder holder, int position) {
        Meal state = meals.get(position);
        holder.imageView.setImageResource(state.getImage());
        holder.nameView.setText(state.getName());
        holder.descriptionView.setText(state.getDescription());
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, descriptionView;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            nameView = view.findViewById(R.id.nameTextView);
            descriptionView = view.findViewById(R.id.descriptionTextView);
        }
    }
}