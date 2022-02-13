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

public class HomeFragment extends Fragment {

    ArrayList <OffersModels> offersModels = new ArrayList<>();
    RecyclerView recyclerOffers;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        //find element recyclerOffers
        recyclerOffers = root.findViewById(R.id.recycle_offers);

        // add item to recyclerOffers
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));


        //put LinearLayoutManager to recyclerView
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter
        recyclerOffers.setAdapter(new OffersAdpter(offersModels,getContext()));

        return root;

    }
}