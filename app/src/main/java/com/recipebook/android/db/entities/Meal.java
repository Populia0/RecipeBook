package com.recipebook.android.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Meals")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_favorite")
    private int isFavorite;

    @ColumnInfo(name = "img_uri")
    private String imgUri;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int isFavorite() { return isFavorite; }
    public void setIsFavorite(int favorite) { isFavorite = favorite; }
    public String getImgUri() { return imgUri; }
    public void setImgUri(String imgUri) { this.imgUri = imgUri; }
}
