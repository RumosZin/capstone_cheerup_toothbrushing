package com.lite.holistic_tracking.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lite.holistic_tracking.Entity.Seed;

import java.util.List;

@Dao
public interface SeedDao {

    @Insert
    void insert(Seed seed);

    @Query("SELECT * FROM Seed")
    List<Seed> getAllSeeds();

    @Query("SELECT * FROM Seed WHERE childName = :childName")
    Seed getSeedByChildName(String childName);

    @Query("UPDATE Seed SET plant = :plantCount WHERE childName = :childName")
    void updatePlantCount(String childName, int plantCount);

    @Query("UPDATE Seed SET flower = :flowerCount WHERE childName = :childName")
    void updateFlowerCount(String childName, int flowerCount);

    @Query("UPDATE Seed SET tree = :treeCount WHERE childName = :childName")
    void updateTreeCount(String childName, int treeCount);

    @Query("DELETE FROM Seed")
    void deleteAllSeeds();

    @Query("UPDATE Seed SET teethPlant = :teethPlant WHERE childName = :childName")
    void updateTeethPlantCount(String childName, int teethPlant);

    @Query("UPDATE Seed SET teethFlower = :teethFlower WHERE childName = :childName")
    void updateTeethFlowerCount(String childName, int teethFlower);

    @Query("UPDATE Seed SET teethTree = :teethTree WHERE childName = :childName")
    void updateTeethTreeCount(String childName, int teethTree);
}
