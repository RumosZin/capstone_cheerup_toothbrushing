package com.lite.holistic_tracking.Database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Song;


@Database(entities = {Song.class}, version = 1)
public abstract class SongDB extends RoomDatabase {

    private static SongDB INSTANCE = null;
    public abstract SongDao songDao();

    // getInstance로 DB 객체를 return 함
    public static SongDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    SongDB.class, "song.db").build();

        }
        return INSTANCE;
    }
}
