package com.lite.holistic_tracking.Database;
import com.lite.holistic_tracking.Entity.Morebrushing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MorebrushingDao {
    @Insert
    void insert(Morebrushing morebrushing);

    @Query("SELECT * FROM morebrushing")
    List<Morebrushing> getAll();

    @Query("DELETE FROM morebrushing")
    void deleteAll();

    @Query("UPDATE morebrushing SET left_circular = 1 WHERE childName = :childName")
    void updateLeftCircularToOne(String childName);

    @Query("UPDATE morebrushing SET mid_circular = 1 WHERE childName = :childName")
    void updateMidCircularToOne(String childName);

    @Query("UPDATE morebrushing SET right_circular = 1 WHERE childName = :childName")
    void updateRightCircularToOne(String childName);

    @Query("UPDATE morebrushing SET left_upper = 1 WHERE childName = :childName")
    void updateLeftUpperToOne(String childName);

    @Query("UPDATE morebrushing SET left_lower = 1 WHERE childName = :childName")
    void updateLeftLowerToOne(String childName);

    @Query("UPDATE morebrushing SET right_upper = 1 WHERE childName = :childName")
    void updateRightUpperToOne(String childName);

    @Query("UPDATE morebrushing SET right_lower = 1 WHERE childName = :childName")
    void updateRightLowerToOne(String childName);

    @Query("UPDATE morebrushing SET mid_vertical_upper = 1 WHERE childName = :childName")
    void updateMidVerticalUpperToOne(String childName);

    @Query("UPDATE morebrushing SET mid_vertical_lower = 1 WHERE childName = :childName")
    void updateMidVerticalLowerToOne(String childName);


}
