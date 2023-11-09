package com.lite.holistic_tracking.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "toothbrushing")
public class Toothbrushing {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "childName")
    private String childName;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

//    @ColumnInfo(name = "areaCounts")
//    private String areaCounts;

    @ColumnInfo(name = "score")
    private int score;

    // 생성자, getter, setter 등 필요한 메서드 작성

    // 예시 생성자, 여기에 영역별 횟수를 추가해야 함
    public Toothbrushing(String childName, String date, String time, int score) {
        this.childName = childName;
        this.date = date;
        this.time = time;
        //this.areaCounts = areaCounts;
        this.score = score;
    }
}