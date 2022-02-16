package com.HomeGarage.garage.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {GrageInfo.class,Opreation.class},version = 3,exportSchema = false)
@TypeConverters(DateConvrter.class)
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
                sInstance= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,AppDataBase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }
    public abstract DAO grageDAO();
}
