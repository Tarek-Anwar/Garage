package com.HomeGarage.garage.DB;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExcutor {
    private static final Object LOCK=new Object();
    private static AppExcutor sInstence;
    private final Executor diskIO;

    public AppExcutor(Executor diskIO) {
        this.diskIO = diskIO;
    }
    public static AppExcutor getInstance()
    {
        if (sInstence==null)
        {
            synchronized (LOCK)
            {
                sInstence=new AppExcutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstence;
    }
    public Executor getDiskIO(){return diskIO;}
}
