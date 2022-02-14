package com.HomeGarage.garage.DB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddGrageViewModel extends ViewModel {
    private LiveData<GrageInfo> grageInfoLiveData;
    public AddGrageViewModel(AppDataBase dataBase,int grageID)
    {
        grageInfoLiveData=dataBase.grageDAO().loadGrageByID(grageID);
    }

    public LiveData<GrageInfo> getGrageInfoLiveData() {
        return grageInfoLiveData;
    }
}
