package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.SearchModel;

public class OperationsFragment extends Fragment {

    SearchModel searchModel;
    public OperationsFragment(SearchModel searchModel) {
        this.searchModel = searchModel;   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view =  inflater.inflate(R.layout.fragment_operations, container, false);

        return  view;
    }
}