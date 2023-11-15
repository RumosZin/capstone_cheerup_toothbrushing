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

    @Query("DELETE FROM toothbrushing")
    void deleteAll();

    @Query("SELECT * FROM toothbrushing WHERE childName = :childName ORDER BY date DESC, time DESC LIMIT 1")
    List<Toothbrushing> getChildToothbrushing(String childName);

    @Query("SELECT * FROM toothbrushing WHERE childName = :childName ORDER BY date DESC, time DESC")
    List<Toothbrushing> getChildToothbrushingAll(String childName);

    @Query("DELETE FROM toothbrushing WHERE id = :id")
    void deleteId(int id);
}
