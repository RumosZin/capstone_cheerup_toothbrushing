package com.lite.holistic_tracking.Database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Seed;

@Database(entities = {Seed.class}, version = 1)
public abstract class SeedDB extends RoomDatabase {

    public abstract SeedDao seedDao();

    private static volatile SeedDB INSTANCE;

    public static SeedDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SeedDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    SeedDB.class, "seed_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
