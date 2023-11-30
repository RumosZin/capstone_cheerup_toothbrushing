package com.lite.holistic_tracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Morebrushing;

@Database(entities = {Morebrushing.class}, version = 1)
public abstract class MorebrushingDB extends RoomDatabase {

    private static MorebrushingDB INSTANCE;
    public abstract MorebrushingDao morebrushingDao();

    public static MorebrushingDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MorebrushingDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MorebrushingDB.class, "morebrushing_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}