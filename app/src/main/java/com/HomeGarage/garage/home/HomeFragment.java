package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.FindModels;
import com.HomeGarage.garage.home.models.OffersModels;
import com.HomeGarage.garage.home.Adapter.*;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList <OffersModels> offersModels = new ArrayList<>();
    ArrayList <FindModels> findModelslist = new ArrayList<>();

    RecyclerView recyclerOffers , recyclerFind;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));

        findModelslist.add(new FindModels(R.drawable.location_searching_icon, getString(R.string.find_in_all_garage)));
        findModelslist.add(new FindModels(R.drawable.search_db_icon,getString(R.string.nearest_garage)));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);


        //find element recyclerOffers
        recyclerOffers = root.findViewById(R.id.recycle_offers);
        //find element recyclerFind
        recyclerFind = root.findViewById(R.id.recycler_find);

        // add item to recyclerOffers



        //put LinearLayoutManager to recyclerOffers
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter recyclerOffers
        recyclerOffers.setAdapter(new OffersAdpter(offersModels,getContext()));

        //put LinearLayoutManager to recyclerFind
        recyclerFind.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter recyclerFind
        recyclerFind.setAdapter(new FindAdpter(getContext(),findModelslist));

        return root;

    }



}