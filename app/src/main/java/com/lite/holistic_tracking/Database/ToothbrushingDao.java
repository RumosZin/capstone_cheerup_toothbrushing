package com.lite.holistic_tracking.Database;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToothbrushingDao {
    @Insert
    void insert(Toothbrushing toothbrushing);

    @Query("SELECT * FROM toothbrushing")
    List<Toothbrushing> getAll();
}
