package com.HomeGarage.garage.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private LiveData<List<GrageInfo>> grages;
    private LiveData<List<Opreation>> opreations;

    public DBViewModel(@NonNull Application application) {
        super(application);

        AppDataBase dataBase=AppDataBase.getInstance(this.getApplication());

        grages=dataBase.grageDAO().loadGrages();
        opreations=dataBase.grageDAO().loadLastOptreations();
    }

    public LiveData<List<GrageInfo>> getGrages(){return grages;}
    public LiveData<List<Opreation>> getOpreations(){return  opreations;}


}
