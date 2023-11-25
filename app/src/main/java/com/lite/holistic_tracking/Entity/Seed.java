package com.lite.holistic_tracking.Entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Seed {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String childName;
    private int plant;
    private int flower;
    private int tree;

    private int teethPlant;
    private int teethFlower;
    private int teethTree;



    public void setTeethTree(int teethTree) {
        this.teethTree = teethTree;
    }

    public Seed(String childName, int plant, int flower, int tree,
                  int teethPlant, int teethFlower, int teethTree) {
        this.childName = childName;
        this.plant = plant;
        this.flower = flower;
        this.tree = tree;
        this.teethPlant = teethPlant;
        this.teethFlower = teethFlower;
        this.teethTree = teethTree;
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

    public int getTeethPlant() {
        return teethPlant;
    }

    public void setTeethPlant(int teethPlant) {
        this.teethPlant = teethPlant;
    }

    public int getTeethFlower() {
        return teethFlower;
    }

    public void setTeethFlower(int teethFlower) {
        this.teethFlower = teethFlower;
    }

    public int getTeethTree() {
        return teethTree;
    }
}
