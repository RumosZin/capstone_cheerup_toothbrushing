package com.lite.holistic_tracking.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lite.holistic_tracking.Entity.Forest;

import java.util.List;

@Dao
public interface ForestDao {

    @Insert
    void insert(Forest forest);

    @Query("SELECT * FROM Forest")
    List<Forest> getAllForests();

    @Query("SELECT * FROM Forest WHERE childName = :childName")
    Forest getForestByChildName(String childName);

    @Query("UPDATE Forest SET plant = :plantCount WHERE childName = :childName")
    void updatePlantCount(String childName, int plantCount);

    @Query("UPDATE Forest SET flower = :flowerCount WHERE childName = :childName")
    void updateFlowerCount(String childName, int flowerCount);

    @Query("UPDATE Forest SET tree = :treeCount WHERE childName = :childName")
    void updateTreeCount(String childName, int treeCount);
}
