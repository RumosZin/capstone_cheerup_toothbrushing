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

}
