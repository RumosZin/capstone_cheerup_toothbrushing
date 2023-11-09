package com.lite.holistic_tracking.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lite.holistic_tracking.Entity.TotalBrushing;

@Dao
public interface TotalBrushingDao {

    @Insert
    void insertTotalBrushing(TotalBrushing totalBrushing);

    @Query("SELECT * FROM TotalBrushing WHERE childName = :childName")
    TotalBrushing getTotalBrushingByChildName(String childName);

}
