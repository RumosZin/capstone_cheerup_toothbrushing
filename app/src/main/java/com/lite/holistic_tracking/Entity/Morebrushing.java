package com.lite.holistic_tracking.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "morebrushing")
public class Morebrushing {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "childName")
    private String childName;

    @ColumnInfo(name = "left_circular")
    private int left_circular;

    @ColumnInfo(name = "mid_circular")
    public int mid_circular;

    @ColumnInfo(name = "right_circular")
    public int right_circular;

    @ColumnInfo(name = "left_upper")
    public int left_upper;

    @ColumnInfo(name = "left_lower")
    public int left_lower;

    @ColumnInfo(name = "right_upper")
    public int right_upper;

    @ColumnInfo(name = "right_lower")
    public int right_lower;

    @ColumnInfo(name = "mid_vertical_upper")
    public int mid_vertical_upper;

    @ColumnInfo(name = "mid_vertical_lower")
    public int mid_vertical_lower;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getLeft_circular() {
        return left_circular;
    }

    public void setLeft_circular(int left_circular) {
        this.left_circular = left_circular;
    }

    public int getMid_circular() {
        return mid_circular;
    }

    public void setMid_circular(int mid_circular) {
        this.mid_circular = mid_circular;
    }

    public int getRight_circular() {
        return right_circular;
    }

    public void setRight_circular(int right_circular) {
        this.right_circular = right_circular;
    }

    public int getLeft_upper() {
        return left_upper;
    }

    public void setLeft_upper(int left_upper) {
        this.left_upper = left_upper;
    }

    public int getLeft_lower() {
        return left_lower;
    }

    public void setLeft_lower(int left_lower) {
        this.left_lower = left_lower;
    }

    public int getRight_upper() {
        return right_upper;
    }

    public void setRight_upper(int right_upper) {
        this.right_upper = right_upper;
    }

    public int getRight_lower() {
        return right_lower;
    }

    public void setRight_lower(int right_lower) {
        this.right_lower = right_lower;
    }

    public int getMid_vertical_upper() {
        return mid_vertical_upper;
    }

    public void setMid_vertical_upper(int mid_vertical_upper) {
        this.mid_vertical_upper = mid_vertical_upper;
    }

    public int getMid_vertical_lower() {
        return mid_vertical_lower;
    }

    public void setMid_vertical_lower(int mid_vertical_lower) {
        this.mid_vertical_lower = mid_vertical_lower;
    }

    // 예시 생성자, 여기에 영역별 횟수를 추가해야 함
    public Morebrushing(String childName,
                         int left_circular, int mid_circular, int right_circular,
                         int left_upper, int left_lower, int right_upper, int right_lower,
                         int mid_vertical_upper, int mid_vertical_lower) {
        this.childName = childName;
        this.left_circular = left_circular;
        this.mid_circular = mid_circular;
        this.right_circular = right_circular;
        this.left_upper = left_upper;
        this.left_lower = left_lower;
        this.right_upper = right_upper;
        this.right_lower = right_lower;
        this.mid_vertical_upper = mid_vertical_upper;
        this.mid_vertical_lower = mid_vertical_lower;
    }
}