package com.HomeGarage.garage.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DAO {

        //grage info
        @Query("SELECT * FROM entity")
        LiveData<List<GrageInfo>> loadGrages();

        @Insert
        void insertGrage(GrageInfo grageInfo);

        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateGrage(GrageInfo grageInfo);

        @Delete
        void deleteGrage(GrageInfo grageInfo);

        @Query("Select * from entity where ID=:id ")
        LiveData<GrageInfo> loadGrageByID(int id);

        //last opreation
        @Query("SELECT * FROM opEntity")
        LiveData<List<Opreation>> loadLastOptreations();

        @Insert
        void insertOpreation(Opreation opreation);

        @Update(onConflict = OnConflictStrategy.REPLACE)
        void updateOpreation(Opreation opreation);

        @Delete
        void deletOpreation(Opreation opreation);

        @Query("select * from opEntity where ID=:id")
        LiveData<Opreation> loadOpreationByID(int id);
}

