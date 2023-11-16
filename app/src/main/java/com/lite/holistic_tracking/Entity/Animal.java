package com.lite.holistic_tracking.Entity;// Animal.java
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Animal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int imageResource;
    private String name;
    private int requiredSeed;

    public Animal(int imageResource, String name, int requiredSeed) {
        this.imageResource = imageResource;
        this.name = name;
        this.requiredSeed = requiredSeed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public int getRequiredSeed() {
        return requiredSeed;
    }
}
