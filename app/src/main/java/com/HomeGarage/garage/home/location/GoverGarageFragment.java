package com.HomeGarage.garage.home.location;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.CityAdapter;


public class GoverGarageFragment extends Fragment implements CityAdapter.CityListener {

    int pos;
    public GoverGarageFragment(int pos) {this.pos=pos; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_gover_garage, container, false);

        CityAdapter cityAdapter = new CityAdapter(pos,this::onCityListener);

        RecyclerView recyclerCity = root.findViewById(R.id.recycle_city);
        recyclerCity.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerCity.setAdapter(cityAdapter);

        return root;
    }

    @Override
    public void onCityListener(String s) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new CityGarageFragment(s));
        transaction.addToBackStack(null);
        transaction.commit();
    }
}