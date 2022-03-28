package com.HomeGarage.garage.home.location;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.GarageInCityAdapter;
import com.HomeGarage.garage.home.GarageViewFragment;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.home.MapsFragment;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CityGarageFragment extends Fragment {

    String citySearch;
    MapsFragment mapsFragment;
    ArrayList<GrageInfo> grageInfos ;
    RecyclerView recyclerView;
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

        if(savedInstanceState==null){
            mapsFragment = new MapsFragment();
            FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction2.replace(R.id.map_gover,mapsFragment);
            transaction2.commit();
        }

        getAllGarage(grageInfos -> {
            if(citySearch!=null){
                mapsFragment.setGover(citySearch);
            }
            mapsFragment.setMarkers(grageInfos);

            recyclerView = root.findViewById(R.id.recycle_garage_in_city);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
            recyclerView.setAdapter(new GarageInCityAdapter(grageInfos ,getContext(), grageInfo -> {
                GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }));

        });
        return root;
    }

    private void getAllGarage(HomeFragment.OnDataReceiveCallback callback) {
        grageInfos = new ArrayList<>();
        Query query = FirebaseUtil.referenceGarage.orderByChild("cityEn").equalTo(citySearch);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GrageInfo grage = dataSnapshot.getValue(GrageInfo.class);
                        grageInfos.add(grage);
                    }
                    callback.onDataReceived(grageInfos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }

        });
    }
}