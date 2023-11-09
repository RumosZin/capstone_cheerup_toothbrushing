package com.lite.holistic_tracking.Entity;

// TotalBrushing.java
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TotalBrushing")
public class TotalBrushing {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "childName")
    public String childName;

    @ColumnInfo(name = "left_circular")
    public int left_circular;

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

}
