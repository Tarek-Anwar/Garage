package com.HomeGarage.garage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.AppExcutor;
import com.HomeGarage.garage.DB.GrageInfo;


public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    private  AppDataBase dataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_search, container, false);

        return root;
    }

    //insert data into DB
    public  void insertData()
    {
        dataBase=AppDataBase.getInstance(getContext());
        GrageInfo grageInfo=new GrageInfo("name","governoate","city",
                "rest of address","location",2.00,3.00,R.id.image);
        AppExcutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<=15;i++) {
                    dataBase.grageDAO().insert(grageInfo);
                }
            }
        });
    }
}