package com.lite.holistic_tracking.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Child {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String childName;
    public String birthdate;
    public String gender;

    public int seed;

    public Integer getId() {
        return id;
    }

    public String getChildName() {
        return childName;
    }
}
