package com.HomeGarage.garage.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GrageDAO {

    @Query("SELECT * FROM entity")
    List<GrageInfo> loadEntity();

    @Insert
    void insert(GrageInfo grageInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(GrageInfo grageInfo);

    @Delete
    void delete(GrageInfo grageInfo);

    @Query("SELECT * FROM ENTITY WHERE ID = :id")
    GrageInfo loadGrageByID(int id);

}

