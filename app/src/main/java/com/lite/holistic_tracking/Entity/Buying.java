package com.lite.holistic_tracking.Entity;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.Entity;

@Entity(tableName = "Buying")
public class Buying {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="childName")
    private String childName;

    @ColumnInfo(name="animalName")
    private String animalName;


    public int getId() {
        return id;
    }

    public String getChildName() {
        return childName;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
