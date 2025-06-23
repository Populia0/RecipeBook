package com.recipebook.android.ui.favourites;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.recipebook.android.R;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.ui.search.MealAdapter;
import com.recipebook.android.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMealAdapter extends RecyclerView.Adapter<FavouriteMealAdapter.FavouriteMealViewHolder> {
    private final Context context;
    private List<Meal> meals;
    private final FavouritesViewModel viewModel;

    public FavouriteMealAdapter(Context context, FavouritesViewModel viewModel) {
        this.context = context;
        this.meals = new ArrayList<>();
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public FavouriteMealAdapter.FavouriteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new FavouriteMealAdapter.FavouriteMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMealAdapter.FavouriteMealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.name.setText(meal.getName());
        holder.description.setText(meal.getDescription());

        String imgUri = meal.getImgUri();
        if (imgUri != null && !imgUri.isEmpty()) {
            try {
                holder.image.setImageURI(Uri.parse(imgUri));
            } catch (Exception e) {
                holder.image.setImageResource(R.drawable.test); // запасная картинка
            }
        } else {
            holder.image.setImageResource(R.drawable.test); // если нет URI
        }

        holder.isFavoriteCheckBox.setOnCheckedChangeListener(null);

        holder.isFavoriteCheckBox.setChecked(meal.isFavorite() == 1);

        holder.isFavoriteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.toggleIsFavoriteCheckBox(meal);
        });
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class FavouriteMealViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        ImageView image;
        CheckBox isFavoriteCheckBox;

        public FavouriteMealViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            image = itemView.findViewById(R.id.imageView);
            isFavoriteCheckBox = itemView.findViewById(R.id.isFavoriteCheckBox);
        }
    }
}
