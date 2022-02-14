package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.DBViewModel;
import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.SearchAdapter;
import com.HomeGarage.garage.home.models.SearchModel;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchAdapter.SearchListener {

    RecyclerView recyclerSearch;
    AppDataBase dataBase;
    SearchAdapter searchAdapter;

    public SearchFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    dataBase=AppDataBase.getInstance(getContext());
    searchAdapter=new SearchAdapter(getContext(),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = root.findViewById(R.id.recycle_search);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerSearch.setAdapter(searchAdapter);
        setUpViewModel();
        return root;
    }

    public void setUpViewModel()
    {
        DBViewModel dbViewModel=new ViewModelProvider(this).get(DBViewModel.class);
        final LiveData<List<GrageInfo>> grages=dbViewModel.getGrages();
        grages.observe(getViewLifecycleOwner(), new Observer<List<GrageInfo>>() {
            @Override
            public void onChanged(List<GrageInfo> grageInfos) {
               searchAdapter.setGrageInfos(grageInfos);
            }
        });
    }
    @Override
    public void SearchListener(GrageInfo grageInfo) {
        Toast.makeText(getContext(), grageInfo.getGrageName(), Toast.LENGTH_SHORT).show();
    }
}