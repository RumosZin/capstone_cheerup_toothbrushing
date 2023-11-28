package com.lite.holistic_tracking.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private int imageResource;
    private String title;
    private int bpm;
    private int songLength;
    private int level;

    public Song(int imageResource, String title, int bpm, int songLength, int level) {
        this.imageResource = imageResource;
        this.title = title;
        this.bpm = bpm;
        this.songLength = songLength;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getSongLength() {
        return songLength;
    }

    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
