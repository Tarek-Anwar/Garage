package com.HomeGarage.garage.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GrageInfo.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static final String LOG_TAG=AppDataBase.class.getSimpleName();
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="Grage_DB";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context)
    {
        if(sInstance==null)
        {
            synchronized (LOCK)
            {
                sInstance= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,AppDataBase.DATABASE_NAME).allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }
    public abstract GrageDAO grageDAO();
}
