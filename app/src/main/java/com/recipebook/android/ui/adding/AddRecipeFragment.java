package com.recipebook.android.ui.adding;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.recipebook.android.R;
import com.recipebook.android.databinding.FragmentAddRecipeBinding;
import com.recipebook.android.db.entities.Ingredient;
import com.recipebook.android.db.entities.Meal;
import com.recipebook.android.db.entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    private final List<Ingredient> selectedIngredients = new ArrayList<>();
    private final List<Tag> selectedTags = new ArrayList<>();

    private AddRecipeViewModel viewModel;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private Uri selectedImageUri;
    private ImageView imageRecipe;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);

        setupToolbar();
        setupIngredientPicker();
        setupTagPicker();

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(getContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
                    }
                });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imageRecipe.setImageURI(selectedImageUri);
                    }
                });

        imageRecipe = binding.getRoot().findViewById(R.id.image_recipe);
        imageRecipe.setOnClickListener(v -> openImageChooser());

        return binding.getRoot();
    }

    private void openImageChooser() {
        Log.d("PERM_TEST", "openImageChooser called");

        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = READ_MEDIA_IMAGES;
        } else {
            permission = READ_EXTERNAL_STORAGE;
        }

        int check = ContextCompat.checkSelfPermission(requireContext(), permission);
        Log.d("PERM_TEST", "Permission check result: " + check);

        if (check == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERM_TEST", "Permission already granted, opening gallery");
            openGallery();
        } else {
            Log.d("PERM_TEST", "Requesting permission: " + permission);
            requestPermissionLauncher.launch(permission);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }


    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.add_recipe_button);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_recipe_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            saveRecipe();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRecipe() {
        String name = binding.editTextRecipeName.getText().toString().trim();
        String description = binding.editTextRecipeDescription.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Название обязательно", Toast.LENGTH_SHORT).show();
            return;
        }

        Meal meal = new Meal();
        meal.setName(name);
        meal.setDescription(description);

        if (selectedImageUri != null) {
            meal.setImgUri(selectedImageUri.toString());
        }

        viewModel.insertMeal(meal, selectedIngredients);

        Toast.makeText(getContext(), "Рецепт сохранен", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

    private void setupTagPicker() {
        View clickableArea = binding.tagsEditContainer;
        clickableArea.setOnClickListener(v -> showTagDialog());
    }

    private void setupIngredientPicker() {
        View clickableArea = binding.ingredientsEditContainer;
        clickableArea.setOnClickListener(v -> showIngredientDialog());
    }

    private void showIngredientDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ingredient_picker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText search = dialogView.findViewById(R.id.searchInput);
        ChipGroup chipGroup = dialogView.findViewById(R.id.chipGroup);

        final List<Ingredient>[] currentIngredients = new List[]{new ArrayList<>()};
        final List<Ingredient> displayedIngredients = new ArrayList<>();

        viewModel.getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null) {
                currentIngredients[0] = ingredients;
                displayedIngredients.clear();
                displayedIngredients.addAll(ingredients);
                showChips(chipGroup, displayedIngredients, "");
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString().trim().toLowerCase();
                List<Ingredient> filtered = new ArrayList<>();
                for (Ingredient i : currentIngredients[0]) {
                    if (i.getName().toLowerCase().contains(q)) {
                        filtered.add(i);
                    }
                }
                displayedIngredients.clear();
                displayedIngredients.addAll(filtered);
                showChips(chipGroup, displayedIngredients, q);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        dialogView.findViewById(R.id.saveButton).setOnClickListener(v -> {
            updateSelectedChips();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showTagDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tag_picker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText search = dialogView.findViewById(R.id.searchInput);
        ChipGroup chipGroup = dialogView.findViewById(R.id.chipGroup);

        final List<Tag>[] currentTags = new List[]{new ArrayList<>()};
        final List<Tag> displayedTags = new ArrayList<>();

        viewModel.getAllTags().observe(getViewLifecycleOwner(), tags -> {
            if (tags != null) {
                currentTags[0] = tags;
                displayedTags.clear();
                displayedTags.addAll(tags);
                showTagChips(chipGroup, displayedTags, "");
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString().trim().toLowerCase();
                List<Tag> filtered = new ArrayList<>();
                for (Tag t : currentTags[0]) {
                    if (t.getName().toLowerCase().contains(q)) {
                        filtered.add(t);
                    }
                }
                displayedTags.clear();
                displayedTags.addAll(filtered);
                showTagChips(chipGroup, displayedTags, q);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        dialogView.findViewById(R.id.saveButton).setOnClickListener(v -> {
            dialog.dismiss();
            updateSelectedTagChips();
        });

        dialog.show();
    }

    private void showTagChips(ChipGroup group, List<Tag> list, String query) {
        group.removeAllViews();

        for (Tag tag : list) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setCheckable(true);
            chip.setChecked(selectedTags.contains(tag));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTags.add(tag);
                } else {
                    selectedTags.remove(tag);
                }
            });
            group.addView(chip);
        }

        boolean tagExists = false;
        for (Tag t : list) {
            if (t.getName().equalsIgnoreCase(query)) {
                tagExists = true;
                break;
            }
        }

        if (!query.isEmpty() && !tagExists) {
            Chip addChip = new Chip(getContext());
            addChip.setText(R.string.add_text + "\"" + query + "\"");
            addChip.setChipIconResource(R.drawable.ic_add);
            addChip.setChipIconVisible(true);
            addChip.setClickable(true);
            addChip.setCheckable(false);
            addChip.setOnClickListener(v -> {
                Tag newTag = new Tag();
                newTag.setName(query);
                viewModel.insertTag(newTag);  // Нужно реализовать в ViewModel

                selectedTags.add(newTag);

                updateSelectedTagChips();
                group.removeView(addChip);
            });
            group.addView(addChip);
        }
    }

    private void updateSelectedTagChips() {
        ChipGroup group = binding.chipGroupTags;
        group.removeAllViews();
        for (Tag tag : selectedTags) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setCheckable(true);
            chip.setChecked(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    selectedTags.remove(tag);
                    updateSelectedTagChips();
                }
            });
            group.addView(chip);
        }
    }


    private void showChips(ChipGroup group, List<Ingredient> list, String query) {
        group.removeAllViews();

        for (Ingredient ingredient : list) {
            Chip chip = new Chip(getContext());
            chip.setText(ingredient.getName());
            chip.setCheckable(true);
            chip.setChecked(selectedIngredients.contains(ingredient));
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!selectedIngredients.contains(ingredient)) {
                        selectedIngredients.add(ingredient);
                    }
                } else {
                    selectedIngredients.remove(ingredient);
                }
            });
            group.addView(chip);
        }

        boolean ingredientExists = false;
        for (Ingredient ingr : list) {
            if (ingr.getName().equalsIgnoreCase(query)) {
                ingredientExists = true;
                break;
            }
        }
        if (!query.isEmpty() && !ingredientExists) {
            Chip addChip = new Chip(getContext());
            addChip.setText("Добавить \"" + query + "\"");
            addChip.setChipIconResource(R.drawable.ic_add);
            addChip.setChipIconVisible(true);
            addChip.setClickable(true);
            addChip.setCheckable(false);
            addChip.setOnClickListener(v -> {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setName(query);
                viewModel.insertIngredient(newIngredient);
                selectedIngredients.add(newIngredient);
                updateSelectedChips();
                group.removeView(addChip);
            });
            group.addView(addChip);
        }
    }

    private void updateSelectedChips() {
        ChipGroup group = binding.chipGroupIngredients;
        group.removeAllViews();
        for (Ingredient ing : selectedIngredients) {
            Chip chip = new Chip(getContext());
            chip.setText(ing.getName());
            chip.setCheckable(true);
            chip.setChecked(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    selectedIngredients.remove(ing);
                    updateSelectedChips();
                }
            });
            group.addView(chip);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


