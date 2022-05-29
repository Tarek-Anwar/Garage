package com.HomeGarage.garage.ui.location;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.adapter.GarageInCityAdapter;
import com.HomeGarage.garage.ui.home.GarageViewFragment;
import com.HomeGarage.garage.ui.home.MapsFragment;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CityGarageFragment extends Fragment {
    private static final String KEY_CITY_SEARCH = "TAG_CITY_SEARCH";
    String citySearch;
    MapsFragment mapsFragment;
    ArrayList<GarageInfoModule> garageInfoModules;
    RecyclerView recyclerView;
    SetMarkersGarage setMarkersGarage;

    public CityGarageFragment(String citySearch) {
        this.citySearch = citySearch;
    }
    public CityGarageFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_city_garage, container, false);
        recyclerView = root.findViewById(R.id.recycle_garage_in_city);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        if(savedInstanceState!=null) savedInstanceState.getString(KEY_CITY_SEARCH);

        setMarkersGarage = grageInfos -> {
            if(getActivity()!=null){
                mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentById(R.id.map_gover);
                mapsFragment.setGover(citySearch);
                mapsFragment.setMarkers(grageInfos,false);
                recyclerView.setAdapter(new GarageInCityAdapter(grageInfos , getContext(), grageInfo -> {
                    replaceFragment(new GarageViewFragment(grageInfo));
                }));
            }
        };

        getAllGarage();

        return root;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getAllGarage() {
        garageInfoModules = FirebaseUtil.allGarage;
        Query query = FirebaseUtil.referenceGarage.orderByChild("cityEn").equalTo(citySearch);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    garageInfoModules.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GarageInfoModule garage = dataSnapshot.getValue(GarageInfoModule.class);
                        garageInfoModules.add(garage);
                    }
                    setMarkersGarage.setMarkersMap(garageInfoModules);
                }
            }
            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new DatabaseException(error.getMessage());
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CITY_SEARCH,citySearch);
    }

    interface  SetMarkersGarage{
        void setMarkersMap( ArrayList<GarageInfoModule> garageInfoModules);
    }
}