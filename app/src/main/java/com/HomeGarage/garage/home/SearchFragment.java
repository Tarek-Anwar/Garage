package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.DBViewModel;
import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.SearchAdapter;

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
        //find recyle xml
        recyclerSearch = root.findViewById(R.id.recycle_search);
        // set adpter
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
        GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}