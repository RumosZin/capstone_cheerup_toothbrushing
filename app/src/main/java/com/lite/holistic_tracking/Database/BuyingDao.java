package com.lite.holistic_tracking.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lite.holistic_tracking.Entity.Buying;

import java.util.List;

@Dao
public interface BuyingDao {

    @Insert
    void insert(Buying buying);

    @Query("SELECT * FROM Buying WHERE childName = :childName")
    List<Buying> getBuyingsByChildName(String childName);

    @Query("DELETE FROM Buying")
    void deleteAllBuyings();

    @Query("SELECT animalName FROM Buying WHERE childName = :childName")
    List<String> getBoughtAnimals(String childName);
}
