package com.example.employee_app;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Staff.class}, version = 2) // I-update kini ngadto sa version 2
public abstract class StaffDatabase extends RoomDatabase {

    public abstract StaffDAO staffDAO();

    private static StaffDatabase instance;

    public static synchronized StaffDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            StaffDatabase.class, "staff_database")
                    .fallbackToDestructiveMigration() // Kini mopapas sa karaan nga data para dili mag-crash
                    .build();
        }
        return instance;
    }
}