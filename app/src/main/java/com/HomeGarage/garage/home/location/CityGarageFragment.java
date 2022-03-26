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

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.GarageInCityAdapter;
import com.HomeGarage.garage.home.GarageViewFragment;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CityGarageFragment extends Fragment {

    String citySearch;
    public CityGarageFragment( String citySearch) {
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
        RecyclerView recyclerView = root.findViewById(R.id.recycle_garage_in_city);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        recyclerView.setAdapter(new GarageInCityAdapter(citySearch, grageInfo -> {
            GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }));

        return root;
    }


}