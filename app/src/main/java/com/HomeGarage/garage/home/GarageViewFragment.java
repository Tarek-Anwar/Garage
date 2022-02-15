package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.HomeGarage.garage.R;

public class GarageViewFragment extends Fragment {

    private TextView nameGarage , totalAddressGarage;
    private Button  orderGarage , showLocationGarage;
    private RatingBar ratingGarage;
    public GarageViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_garage_view, container, false);
        initView(root);
        ratingGarage.setRating(1.5F);
        ratingGarage.setEnabled(false);
        nameGarage.setText("El Waha Garage");
        totalAddressGarage.setText("15 str Geham ,El Mansora , El Dokhlya");
        return root;
    }

    void initView (View view){
        nameGarage = view.findViewById(R.id.name_garage_txt);
        totalAddressGarage = view.findViewById(R.id.total_address_garage_txt);
        orderGarage = view.findViewById(R.id.btn_order_garage);
        showLocationGarage = view.findViewById(R.id.btn_show_loca_garage);
        ratingGarage = view.findViewById(R.id.rating_garage);
    }
}