package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.OffersModels;

public class OffersFragment extends Fragment {

    OffersModels offersModels;
    TextView txtOfferDetils;
    ImageView imageOffers;

    public OffersFragment( OffersModels offersModels) {
        this.offersModels = offersModels;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_offers, container, false);
        initViews(root);
        return root;
    }

    private void initViews(View root) {

        txtOfferDetils = root.findViewById(R.id.txt_offer_detils);
        txtOfferDetils.setText(offersModels.getTxtDetils());

        imageOffers = root.findViewById(R.id.image_offers_frggment);
        imageOffers.setImageResource(offersModels.getImg());

    }


}