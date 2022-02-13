package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.OffersModels;
import com.HomeGarage.garage.home.Adapter.*;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ArrayList <OffersModels> offersModels = new ArrayList<>();
    RecyclerView recyclerView;
    public HomeFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycle_offers);

        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));


        offersAdpter adapter = new offersAdpter(offersModels,getContext());
        //put LinearLayoutManager to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter
        recyclerView.setAdapter(adapter);
        return root;

    }
}