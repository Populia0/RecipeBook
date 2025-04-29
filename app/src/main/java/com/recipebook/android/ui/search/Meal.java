package com.recipebook.android.ui.search;

public class Meal {

    private String name; // название
    private String description;  // описание
    private int image; // фото

    public Meal(String name, String description, int image){

        this.name=name;
        this.description=description;
        this.image=image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
