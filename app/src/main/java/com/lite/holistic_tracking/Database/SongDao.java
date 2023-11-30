package com.lite.holistic_tracking.Database;
import com.lite.holistic_tracking.Entity.Song;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDao {

    @Insert
    void insert(Song song);

    @Query("SELECT * FROM Song")
    List<Song> getAllSongs();

    @Query("SELECT * FROM Song WHERE title = :title")
    Song getSongByTitle(String title);

    @Query("SELECT COUNT(*) FROM Song")
    int getSongCount();

    @Query("DELETE FROM Song")
    void deleteAllSongs();

}
