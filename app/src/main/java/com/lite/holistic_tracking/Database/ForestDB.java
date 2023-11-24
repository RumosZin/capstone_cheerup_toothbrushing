package com.lite.holistic_tracking.Database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Forest;

@Database(entities = {Forest.class}, version = 1)
public abstract class ForestDB extends RoomDatabase {

    public abstract ForestDao forestDao();

    private static volatile ForestDB INSTANCE;

    public static ForestDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ForestDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ForestDB.class, "forest_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
