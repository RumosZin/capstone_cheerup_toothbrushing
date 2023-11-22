package com.lite.holistic_tracking.Database;
import com.lite.holistic_tracking.Entity.Child;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ChildDao {
    @Insert
    void insertChild(Child child);

    @Query("SELECT * FROM Child")
    List<Child> getAll();

    @Query("SELECT COUNT(*) FROM Child")
    int getChildCount();

    @Query("DELETE FROM Child")
    void deleteAllChildren();

    @Query("SELECT * FROM Child WHERE childName = :name LIMIT 1")
    Child getChildByName(String name);

    @Query("UPDATE Child SET seed = seed + :newSeed WHERE childName = :name")
    void updateChildSeed(String name, int newSeed);

    @Query("UPDATE Child SET seed = :newSeed WHERE childName = :name")
    void resetChildSeed(String name, int newSeed);
}
