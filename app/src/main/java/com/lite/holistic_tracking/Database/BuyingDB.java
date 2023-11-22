package com.lite.holistic_tracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Buying;

@Database(entities = {Buying.class}, version = 1, exportSchema = false)
public abstract class BuyingDB extends RoomDatabase {

    private static BuyingDB instance;

    public abstract BuyingDao buyingDao();

    public static synchronized BuyingDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BuyingDB.class, "buying_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
