package com.HomeGarage.garage.DB;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddGrageViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int grageID;
    private final AppDataBase dataBase;

    public AddGrageViewModelFactory(int grageID, AppDataBase dataBase) {
        this.grageID = grageID;
        this.dataBase = dataBase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddGrageViewModel(dataBase,grageID);
    }
}
