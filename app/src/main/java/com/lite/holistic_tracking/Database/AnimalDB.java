package com.lite.holistic_tracking.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Animal;

@Database(entities = {Animal.class}, version = 1)
public abstract class AnimalDB extends RoomDatabase {

    private static AnimalDB INSTANCE = null;
    public abstract AnimalDao animalDao();

    // getInstance로 DB 객체를 return 함
    public static AnimalDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AnimalDB.class, "animal.db").build();

        }
        return INSTANCE;
    }
}
