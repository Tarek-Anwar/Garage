package com.HomeGarage.garage.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CityGarageFragment extends Fragment {

    String citySearch;
    MapsFragment mapsFragment;
    ArrayList<GarageInfoModule> garageInfoModules;
    RecyclerView recyclerView;
    SetMarkersGarage setMarkersGarage;

    public CityGarageFragment(String citySearch) {
        this.citySearch = citySearch;
    }

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


        setMarkersGarage = grageInfos -> {
            if(getActivity()!=null){
                mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentById(R.id.map_gover);
                mapsFragment.setGover(citySearch);
                mapsFragment.setMarkers(grageInfos,false);
                recyclerView.setAdapter(new GarageInCityAdapter(grageInfos , getContext(), grageInfo -> {
                    GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }));
            }
        };

        getAllGarage();

        return root;
    }

    private void getAllGarage() {
        garageInfoModules = FirebaseUtil.allGarage;
        Query query = FirebaseUtil.referenceGarage.orderByChild("cityEn").equalTo(citySearch);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    garageInfoModules.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GarageInfoModule grage = dataSnapshot.getValue(GarageInfoModule.class);
                        garageInfoModules.add(grage);
                    }
                    setMarkersGarage.setMarkersMap(garageInfoModules);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });
    }

    interface  SetMarkersGarage{
        void setMarkersMap( ArrayList<GarageInfoModule> garageInfoModules);
    }
}