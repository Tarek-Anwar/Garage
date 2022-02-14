package com.HomeGarage.garage.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {
    private LiveData<List<GrageInfo>> grages;
    public DBViewModel(@NonNull Application application) {
        super(application);
        AppDataBase dataBase=AppDataBase.getInstance(this.getApplication());
        grages=dataBase.grageDAO().loadGrages();
    }
    public LiveData<List<GrageInfo>> getGrages(){return grages;}
}
