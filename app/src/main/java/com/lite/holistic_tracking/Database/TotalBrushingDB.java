package com.lite.holistic_tracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.TotalBrushing;

@Database(entities = {TotalBrushing.class}, version = 1, exportSchema = false)
public abstract class TotalBrushingDB extends RoomDatabase {

    private static TotalBrushingDB INSTANCE = null;

    public abstract TotalBrushingDao totalBrushingDao();

    public static TotalBrushingDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TotalBrushingDB.class,
                            "total_brushing_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
