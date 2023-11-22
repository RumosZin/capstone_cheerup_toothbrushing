package com.lite.holistic_tracking.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lite.holistic_tracking.Entity.Animal;

import java.util.List;

@Dao
public interface AnimalDao {
    @Insert
    void insert(Animal animal);

    @Query("SELECT * FROM Animal")
    List<Animal> getAllAnimals();

    @Query("SELECT * FROM Animal ORDER BY requiredSeed ASC")
    List<Animal> getAll();

    @Query("DELETE FROM Animal")
    void deleteAll();
    @Query("SELECT COUNT(*) FROM Animal")
    int getAnimalCount();
}