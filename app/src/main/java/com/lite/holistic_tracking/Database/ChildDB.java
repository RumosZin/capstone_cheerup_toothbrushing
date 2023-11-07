package com.lite.holistic_tracking.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Entity.Child;

@Database(entities = {Child.class}, version = 1, exportSchema = false)
public abstract class ChildDB extends RoomDatabase {

    private static ChildDB INSTANCE = null;
    public abstract ChildDao childDao();

    // getInstance로 DB 객체를 return 함
    public static ChildDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ChildDB.class, "child.db").build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
