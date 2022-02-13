package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.LastOperModels;
import com.HomeGarage.garage.home.models.OffersModels;
import com.HomeGarage.garage.home.Adapter.*;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList <OffersModels> offersModels = new ArrayList<>();
    ArrayList <LastOperModels> lastOperModelsList = new ArrayList<>();
    RecyclerView recyclerOffers , recyclerFind , recyclerLast;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add item toOffers
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_background));
        offersModels.add(new OffersModels(R.drawable.ic_launcher_foreground));

        // add item to Operaitions
        lastOperModelsList.add(new LastOperModels("Push", "Clien", "Garger Owner",
                "15:3 AM 15-1", "Cairo , 15 Str"));
        lastOperModelsList.add(new LastOperModels("Accpet", "Garger Owner", "Clien",
                "00:5 AM 16-2", "Mahalla , 15 Str"));
        lastOperModelsList.add(new LastOperModels("Push", "Clien", "Garger Owner",
                "9:3 AM 30-3", "Mansora , 20 str"));
        lastOperModelsList.add(new LastOperModels("cancel", "Clien", "Garger Owner",
                "15:3 AM 15-1", "Cairo , 15 Str"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);


        //find element recyclerOffers
        recyclerOffers = root.findViewById(R.id.recycle_offers);
        //find element recyclerFind
        recyclerLast = root.findViewById(R.id.recycler_last);


        //put LinearLayoutManager to recyclerOffers
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter recyclerOffers
        recyclerOffers.setAdapter(new OffersAdpter(offersModels,getContext()));


        //put LinearLayoutManager to recyclerFind
        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
        //set adapter recyclerFind
        recyclerLast.setAdapter(new LastOperAdapter(lastOperModelsList,getContext()));


        return root;

    }

}