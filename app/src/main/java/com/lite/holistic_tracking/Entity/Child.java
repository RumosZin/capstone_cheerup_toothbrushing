package com.lite.holistic_tracking.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Child")
public class Child {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="childName")
    public String childName;
    @ColumnInfo(name="birthdate")
    public String birthdate;
    @ColumnInfo(name="gender")
    public String gender;
    @ColumnInfo(name="seed")
    public int seed;

    public Integer getId() {
        return id;
    }

    public String getChildName() {
        return childName;
    }
    public String getBirthDate() { return birthdate; }

    public String getGender() { return gender; }

    public int getSeed() { return seed;}


    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setBirthDate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }


}
