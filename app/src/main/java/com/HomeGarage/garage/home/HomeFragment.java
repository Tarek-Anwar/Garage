package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.HomeGarage.garage.OffersFragment;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.LastOperModels;
import com.HomeGarage.garage.home.models.OffersModels;
import com.HomeGarage.garage.home.Adapter.*;
import com.HomeGarage.garage.sign.SignUpFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OffersAdpter.OfferListener {

    ArrayList <OffersModels> offersModels = new ArrayList<>();
    ArrayList <LastOperModels> lastOperModelsList = new ArrayList<>();
    RecyclerView recyclerOffers , recyclerLast;
    LinearLayout layoutNearFind , layoutAllFind ;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add item toOffers
        offersModels.add(new OffersModels(R.drawable.offer_crisimis, "Special offer 40% off on the occasion of New Year's Eve on all Cairo financier garages"));
        offersModels.add(new OffersModels(R.drawable.offer_special,"Special offer for the first time using the program"));
        offersModels.add(new OffersModels(R.drawable.offer_get,"Use the program in two payments and get a free process"));
        offersModels.add(new OffersModels(R.drawable.offer_weekend, "Half the price when using the Mall of Arabia garage on the weekends"));
        offersModels.add(new OffersModels(R.drawable.offer_new,"Cash back 10% when using the program daily for a week"));

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

        //find element
        initViews(root);

        //put LinearLayoutManager to recyclerOffers
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter recyclerOffers
        recyclerOffers.setAdapter(new OffersAdpter(offersModels,getContext(),this));

        //put LinearLayoutManager to recyclerFind
        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
        //set adapter recyclerFind
        recyclerLast.setAdapter(new LastOperAdapter(lastOperModelsList,getContext()));


        return root;

    }

    private void initViews(View v){
        recyclerOffers = v.findViewById(R.id.recycle_offers);
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
    }

    @Override
    public void OfferListener(OffersModels offersModels) {

        OffersFragment newFragment = new OffersFragment(offersModels);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}