package com.lite.holistic_tracking.Entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Forest {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String childName;
    private int plant;
    private int flower;
    private int tree;

    public Forest(String childName, int plant, int flower, int tree) {
        this.childName = childName;
        this.plant = plant;
        this.flower = flower;
        this.tree = tree;
    }

    public long getId() {
        return id;
    }

    public String getChildName() {
        return childName;
    }

    public int getPlant() {
        return plant;
    }

    public int getFlower() {
        return flower;
    }

    public int getTree() {
        return tree;
    }

    public void setId(long id) {
        this.id = id;
    }
}
