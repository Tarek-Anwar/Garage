package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.SearchAdapter;
import com.HomeGarage.garage.home.models.Opreation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment implements SearchAdapter.SearchListener {

    RecyclerView recyclerSearch;

    SearchAdapter searchAdapter;
    ArrayList<GrageInfo> grageInfos=new ArrayList<>();
    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchAdapter=new SearchAdapter(getContext(),this);
        insertGrages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_search, container, false);
        //find recycle xml
        recyclerSearch = root.findViewById(R.id.recycle_search);
        // set adapter
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerSearch.setAdapter(searchAdapter);

        return root;
    }
    public void insertGrages() {
        GrageInfo grage=new GrageInfo("grageName", "governoate","city"," restOfAddress","location", 8.00f, 4.5f, R.id.image);
        for (int i=0;i<15;i++)
        {
            grageInfos.add(grage);
        }
        searchAdapter.setGrageInfos(grageInfos);

    }

    @Override
    public void SearchListener(GrageInfo grageInfo) {
        GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}