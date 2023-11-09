package com.lite.holistic_tracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Toothbrushing;

@Database(entities = {Toothbrushing.class}, version = 1)
public abstract class ToothbrushingDB extends RoomDatabase {

    private static ToothbrushingDB INSTANCE;
    public abstract ToothbrushingDao toothbrushingDao();

    public static ToothbrushingDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ToothbrushingDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ToothbrushingDB.class, "toothbrushing_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}